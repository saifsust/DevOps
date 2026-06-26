#!/bin/sh

CERTS_DIR=$(pwd)
IP="192.168.0.144"
DAYS=3650
BITS_SIZE=2048

rm -f $CERTS_DIR/*.crt
rm -f $CERTS_DIR/*.key
rm -f $CERTS_DIR/*.csr
rm -f $CERTS_DIR/*.srl

# Generate CA certificate
openssl genrsa -out $CERTS_DIR/ca.key $BITS_SIZE
openssl req -x509 -key $CERTS_DIR/ca.key -subj="/CN=certificate/O=$IP" -days $DAYS -out $CERTS_DIR/ca.crt

# Generate Elasticsearch certificates
openssl genrsa -out $CERTS_DIR/elasticsearch.key $BITS_SIZE

openssl req \
  -new -key $CERTS_DIR/elasticsearch.key \
  -subj="/CN=elasticsearch/O=$IP" \
  -out $CERTS_DIR/elasticsearch.csr

openssl x509 -req -in $CERTS_DIR/elasticsearch.csr \
  -CA $CERTS_DIR/ca.crt \
  -CAkey $CERTS_DIR/ca.key \
  -days $DAYS \
  -CAcreateserial \
  -extensions v3_req \
  -extfile $CERTS_DIR/elasticsearch-scan.cnf \
  -out $CERTS_DIR/elasticsearch.crt

openssl verify -CAfile $CERTS_DIR/ca.crt $CERTS_DIR/elasticsearch.crt

# Generate Kibana certificates
openssl genrsa -out $CERTS_DIR/kibana.key $BITS_SIZE

openssl req -new -key $CERTS_DIR/kibana.key \
  -subj="/CN=kibana/O=$IP" \
  -out $CERTS_DIR/kibana.csr

openssl x509 -req -in $CERTS_DIR/kibana.csr \
  -CA $CERTS_DIR/ca.crt \
  -CAkey $CERTS_DIR/ca.key \
  -days $DAYS \
  -CAcreateserial \
  -extensions v3_req \
  -extfile $CERTS_DIR/elasticsearch-scan.cnf \
  -out $CERTS_DIR/kibana.crt

openssl verify -CAfile $CERTS_DIR/ca.crt $CERTS_DIR/kibana.crt

# Clean up CSR files
rm -f $CERTS_DIR/*.csr

echo "Certificates generated successfully!"