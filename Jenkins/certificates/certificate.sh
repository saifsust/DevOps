openssl genrsa -aes256 -passout pass:bjit1234 -out ca-key.pem 2048

openssl req -new -x509 -days 3650 -passin pass:bjit1234 -key ca-key.pem -subj="/CN=docker-ca/O=controlplane" -extensions v3_ca -config openssl.cnf -out ca.pem

openssl genrsa -out docker-server-key.pem 2048

openssl req -new -key docker-server-key.pem -subj="/CN=docker-server/O=controlplane" -extensions v3_ca -config openssl.cnf -out docker-server.csr

openssl x509 -days 3650 -req -in docker-server.csr -passin pass:bjit1234 -CA ca.pem -CAkey ca-key.pem -CAcreateserial -extensions v3_ca -extfile openssl.cnf -out docker-server.pem

openssl genrsa -out key.pem 2048

openssl req -new -subj="/CN=client/O=controlplane" -key key.pem -extensions v3_ca -config openssl.cnf -out client.csr

openssl x509 -days 3650 -req -in client.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -passin pass:bjit1234 -extensions v3_ca -extfile openssl.cnf -out cert.pem

rm client.csr docker-server.csr

openssl verify -CAfile ca.pem docker-server.pem
openssl verify -CAfile ca.pem cert.pem


sudo systemctl daemon-reload
sudo systemctl restart docker

openssl s_client -connect 192.168.0.30:2376
