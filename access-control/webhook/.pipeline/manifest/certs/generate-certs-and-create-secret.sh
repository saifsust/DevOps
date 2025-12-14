#!/bin/bash

# CA certificate and key generation
openssl genrsa -aes256 -passout pass:bjit1234 -out ca.key 2048
openssl req -new -x509 -days 3560 -key ca.key -subj="/CN=kubernetes/O=webhook" -passin pass:bjit1234 -out ca.crt

# Client certificate adn key generation
openssl genrsa -out client.key 2048
openssl req -new -key client.key -subj="/CN=$(cat host)/O=webhook" -config csr.conf  -out client.csr
openssl x509 -req -in client.csr -days 3560 -passin pass:bjit1234 -CA ca.crt -CAkey ca.key -extensions v3_req -extfile csr.conf -out client.crt

openssl verify -CAfile ca.crt client.crt

rm *.csr

kubectl -n istio-system create secret tls webhook-credentials --cert=client.crt --key=client.key