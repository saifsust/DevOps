import unittest

import requests_mock

import productpage


class ApplianceTest(unittest.TestCase):

    def setUp(self):
        self.app = productpage.app.test_client()

    @requests_mock.Mocker()
    def test_header_propagation_reviews(self, m):
        """ Check that tracing headers are forwarded correctly """
        product_id = 0
        # Register expected headers with the mock. If the headers
        # don't match, the mock won't fire, an E500 will be triggered
        # and the test will fail.
        expected_headers = {
            'x-request-id': '34eeb41d-d267-9e49-8b84-dde403fc5b72',
            'x-b3-traceid': '80f198ee56343ba864fe8b2a57d3eff7',
            'x-b3-spanid': 'e457b5a2e4d86bd1',
            'x-b3-sampled': '1',
            'sw8': '40c7fdf104e3de67'
        }
        m.get("http://reviews:9080/reviews/%d" % product_id, text='{}',
              request_headers=expected_headers)

        uri = "/api/v1/products/%d/reviews" % product_id
        headers = {
            'x-request-id': '34eeb41d-d267-9e49-8b84-dde403fc5b72',
            'x-b3-traceid': '80f198ee56343ba864fe8b2a57d3eff7',
            'x-b3-spanid': 'e457b5a2e4d86bd1',
            'x-b3-sampled': '1',
            'sw8': '40c7fdf104e3de67'
        }
        actual = self.app.get(uri, headers=headers)
        self.assertEqual(200, actual.status_code)

    @requests_mock.Mocker()
    def test_header_propagation_ratings(self, m):
        """ Check that tracing headers are forwarded correctly """
        product_id = 0
        # Register expected headers with the mock. If the headers
        # don't match, the mock won't fire, an E500 will be triggered
        # and the test will fail.
        expected_headers = {
            'x-request-id': '34eeb41d-d267-9e49-8b84-dde403fc5b73',
            'x-b3-traceid': '80f198ee56343ba864fe8b2a57d3eff7',
            'x-b3-spanid': 'e457b5a2e4d86bd1',
            'x-b3-sampled': '1',
            'sw8': '40c7fdf104e3de67'
        }
        m.get("http://ratings:9080/ratings/%d" % product_id, text='{}',
              request_headers=expected_headers)

        uri = "/api/v1/products/%d/ratings" % product_id
        headers = {
            'x-request-id': '34eeb41d-d267-9e49-8b84-dde403fc5b73',
            'x-b3-traceid': '80f198ee56343ba864fe8b2a57d3eff7',
            'x-b3-spanid': 'e457b5a2e4d86bd1',
            'x-b3-sampled': '1',
            'sw8': '40c7fdf104e3de67'
        }
        actual = self.app.get(uri, headers=headers)
        print(actual.data)
        self.assertEqual(200, actual.status_code)
