import time
from flask import Flask, request, session, render_template, redirect, g
from json2html import json2html
from opentelemetry import trace
from opentelemetry.instrumentation.flask import FlaskInstrumentor
from opentelemetry.propagate import set_global_textmap
from opentelemetry.propagators.b3 import B3MultiFormat
from opentelemetry.sdk.trace import TracerProvider
from prometheus_client import Counter, generate_latest
import asyncio
import logging
import os
import requests
import simplejson as json
import sys

import http.client as http_client
http_client.HTTPConnection.debuglevel = 0

app = Flask(__name__)
FlaskInstrumentor().instrument_app(app)
logging.basicConfig(stream=sys.stdout, level=logging.INFO)
requests_log = logging.getLogger("requests.packages.urllib3")
requests_log.setLevel(logging.INFO)
requests_log.propagate = True
app.logger.addHandler(logging.StreamHandler(sys.stdout))
app.logger.setLevel(logging.INFO)

# Set the secret key to some random bytes. Keep this really secret!
app.secret_key = b'_5#y2L"F4Q8z\n\xec]/'

servicesDomain = "" if (os.environ.get("SERVICES_DOMAIN") is None) else "." + os.environ.get("SERVICES_DOMAIN")
detailsHostname = "details" if (os.environ.get("DETAILS_HOSTNAME") is None) else os.environ.get("DETAILS_HOSTNAME")
detailsPort = "9080" if (os.environ.get("DETAILS_SERVICE_PORT") is None) else os.environ.get("DETAILS_SERVICE_PORT")
ratingsHostname = "ratings" if (os.environ.get("RATINGS_HOSTNAME") is None) else os.environ.get("RATINGS_HOSTNAME")
ratingsPort = "9080" if (os.environ.get("RATINGS_SERVICE_PORT") is None) else os.environ.get("RATINGS_SERVICE_PORT")
reviewsHostname = "reviews" if (os.environ.get("REVIEWS_HOSTNAME") is None) else os.environ.get("REVIEWS_HOSTNAME")
reviewsPort = "9080" if (os.environ.get("REVIEWS_SERVICE_PORT") is None) else os.environ.get("REVIEWS_SERVICE_PORT")

flood_factor = 0 if (os.environ.get("FLOOD_FACTOR") is None) else int(os.environ.get("FLOOD_FACTOR"))

details = {
    "name": "http://{0}{1}:{2}".format(detailsHostname, servicesDomain, detailsPort),
    "endpoint": "details",
    "children": []
}

ratings = {
    "name": "http://{0}{1}:{2}".format(ratingsHostname, servicesDomain, ratingsPort),
    "endpoint": "ratings",
    "children": []
}

reviews = {
    "name": "http://{0}{1}:{2}".format(reviewsHostname, servicesDomain, reviewsPort),
    "endpoint": "reviews",
    "children": [ratings]
}

productpage = {
    "name": "http://{0}{1}:{2}".format(detailsHostname, servicesDomain, detailsPort),
    "endpoint": "details",
    "children": [details, reviews]
}

service_dict = {
    "productpage": productpage,
    "details": details,
    "reviews": reviews,
}

request_result_counter = Counter('request_result', 'Results of requests', ['destination_app', 'response_code'])

propagator = B3MultiFormat()
set_global_textmap(B3MultiFormat())
provider = TracerProvider()
# Sets the global default tracer provider
trace.set_tracer_provider(provider)

tracer = trace.get_tracer(__name__)


def getForwardHeaders(request):
    headers = {}

    # x-b3-*** headers can be populated using the OpenTelemetry span
    ctx = propagator.extract(carrier={k.lower(): v for k, v in request.headers})
    propagator.inject(headers, ctx)

    # We handle other (non x-b3-***) headers manually
    if 'user' in session:
        headers['end-user'] = session['user']

    # Keep this in sync with the headers in details and reviews.
    incoming_headers = [
        'x-request-id',
        'x-ot-span-context',
        'x-datadog-trace-id',
        'x-datadog-parent-id',
        'x-datadog-sampling-priority',
        'traceparent',
        'tracestate',
        'x-cloud-trace-context',
        'grpc-trace-bin',
        'sw8',
        'user-agent',
        'cookie',
        'authorization',
        'jwt',
    ]

    for ihdr in incoming_headers:
        val = request.headers.get(ihdr)
        if val is not None:
            headers[ihdr] = val

    return headers

@app.route('/health')
def health():
    return 'Product page is healthy'

@app.route('/login', methods=['POST'])
def login():
    user = request.values.get('username')
    response = app.make_response(redirect(request.referrer))
    session['user'] = user
    app.logger.info("Login request")

    return response

@app.route('/logout', methods=['GET'])
def logout():
    response = app.make_response(redirect(request.referrer))
    session.pop('user', None)
    app.logger.info("Log out request")
    return response

async def getProductReviewsIgnoreResponse(product_id, headers):
    getProductReviews(product_id, headers)

async def floodReviewsAsynchronously(product_id, headers):
    # the response is disregarded
    await asyncio.gather(*(getProductReviewsIgnoreResponse(product_id, headers) for _ in range(flood_factor)))

def floodReviews(product_id, headers):
    loop = asyncio.new_event_loop()
    loop.run_until_complete(floodReviewsAsynchronously(product_id, headers))
    loop.close()


@app.route('/')
@app.route('/home')
def front():
    product_id = 0  # TODO: replace default value
    headers = getForwardHeaders(request)
    user = session.get('user', '')
    product = getProduct(product_id)
    detailsStatus, details = getProductDetails(product_id, headers)

    if flood_factor > 0:
        floodReviews(product_id, headers)

    reviewsStatus, reviews = getProductReviews(product_id, headers)

    app.logger.info(" Product page !")
    app.logger.info(reviews)

    return render_template(
        'productpage.html',
        detailsStatus=detailsStatus,
        reviewsStatus=reviewsStatus,
        product=product,
        details=details,
        reviews=reviews,
        user=user)


# The API:
@app.route('/api/v1/products')
def productsRoute():
    app.logger.info("products list rest api")
    return json.dumps(getProducts()), 200, {'Content-Type': 'application/json'}


@app.route('/api/v1/products/<product_id>')
def productRoute(product_id):
    app.logger.info("products detail rest api")
    headers = getForwardHeaders(request)
    status, details = getProductDetails(product_id, headers)
    return json.dumps(details), status, {'Content-Type': 'application/json'}


@app.route('/api/v1/products/<product_id>/reviews')
def reviewsRoute(product_id):
    app.logger.info("products review rest api")
    headers = getForwardHeaders(request)
    status, reviews = getProductReviews(product_id, headers)
    return json.dumps(reviews), status, {'Content-Type': 'application/json'}


@app.route('/api/v1/products/<product_id>/ratings')
def ratingsRoute(product_id):
    app.logger.info("products ratings rest api")
    headers = getForwardHeaders(request)
    status, ratings = getProductRatings(product_id, headers)
    return json.dumps(ratings), status, {'Content-Type': 'application/json'}


@app.route('/metrics')
def metrics():
    return generate_latest()


# Data providers:
def getProducts():
    return [
        {
            'id': 0,
            'title': 'The Comedy of Errors',
            'descriptionHtml': '<a href="https://en.wikipedia.org/wiki/The_Comedy_of_Errors">Wikipedia Summary</a>: The Comedy of Errors is one of <b>William Shakespeare\'s</b> early plays. It is his shortest and one of his most farcical comedies, with a major part of the humour coming from slapstick and mistaken identity, in addition to puns and word play.'
        }
    ]


def getProduct(product_id):
    products = getProducts()
    if product_id + 1 > len(products):
        return None
    else:
        return products[product_id]


def getProductDetails(product_id, headers):
    try:
        url = details['name'] + "/" + details['endpoint'] + "/" + str(product_id)
        res = send_request(url, headers=headers, timeout=3.0)
    except BaseException:
        res = None
    if res and res.status_code == 200:
        request_result_counter.labels(destination_app='details', response_code=200).inc()
        return 200, res.json()
    else:
        status = res.status_code if res is not None and res.status_code else 500
        request_result_counter.labels(destination_app='details', response_code=status).inc()
        return status, {'error': 'Sorry, product details are currently unavailable for this book.'}


def getProductReviews(product_id, headers):
    # Do not remove. Bug introduced explicitly for illustration in fault injection task
    # TODO: Figure out how to achieve the same effect using Envoy retries/timeouts
    for _ in range(2):
        try:
            url = reviews['name'] + "/" + reviews['endpoint'] + "/" + str(product_id)
            res = send_request(url, headers=headers, timeout=3.0)
            app.logger.info(res.json())
        except BaseException:
            res = None
        if res and res.status_code == 200:
            request_result_counter.labels(destination_app='reviews', response_code=200).inc()
            return 200, res.json()
    status = res.status_code if res is not None and res.status_code else 500
    request_result_counter.labels(destination_app='reviews', response_code=status).inc()
    return status, {'error': 'Sorry, product reviews are currently unavailable for this book.'}


def getProductRatings(product_id, headers):
    try:
        url = ratings['name'] + "/" + ratings['endpoint'] + "/" + str(product_id)
        res = send_request(url, headers=headers, timeout=3.0)
    except BaseException:
        res = None
    if res and res.status_code == 200:
        request_result_counter.labels(destination_app='ratings', response_code=200).inc()
        return 200, res.json()
    else:
        status = res.status_code if res is not None and res.status_code else 500
        request_result_counter.labels(destination_app='ratings', response_code=status).inc()
        return status, {'error': 'Sorry, product ratings are currently unavailable for this book.'}


def send_request(url, **kwargs):
    # We intentionally do not pool so that we can easily test load distribution across many versions of our backends
    return requests.get(url, **kwargs)


class Writer(object):
    def __init__(self, filename):
        self.file = open(filename, 'w')

    def write(self, data):
        self.file.write(data)

    def flush(self):
        self.file.flush()


if __name__ == '__main__':
    if len(sys.argv) < 2:
        logging.error("usage: %s port" % (sys.argv[0]))
        sys.exit(-1)

    p = int(sys.argv[1])
    logging.info("start at port %s" % (p))
    # Make it compatible with IPv6 if Linux
    if sys.platform == "linux":
        app.run(host='::', port=p, debug=False, threaded=True)
    else:
        app.run(host='0.0.0.0', port=p, debug=False, threaded=True)
