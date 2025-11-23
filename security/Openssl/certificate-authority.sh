# pass need when self authorized certificates are generated.
openssl genrsa -aes256 -passout pass:1234 -out ca.key 2048
# Authority Certificate creation
openssl req -new -x509 -days 3650 -passin pass:1234 -subj="/CN=192.168.0.32/O=kubernetes" -key ca.key -out ca.crt

# dockerd
openssl genrsa -out dockerd.key 2048
openssl req -new -key dockerd.key -subj="/CN=dockerd/O=system:nodes" -out dockerd.csr
openssl x509 -days 20 -req -in dockerd.csr -passin pass:1234  -CA ca.crt -CAkey ca.key -CAcreateserial -out dockerd.crt

# client certificate creation
openssl genrsa -out client.key 2048
openssl req -new -key client.key -subj="/CN=client/O=system:nodes" -out client.csr
openssl x509 -req -in client.csr -days 34 -CA ca.crt -CAkey ca.key -CAcreateserial -passin pass:1234  -out client.crt