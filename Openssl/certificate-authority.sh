# pass need when self authorized certificates are generated.
openssl genrsa -aes256 -passout pass:1234 -out ca.key 2048
# Authority Certificate creation
openssl req -new -x509 -days 3650 -passin pass:1234 -subj="/CN=192.168.0.32/O=kubernetes" -key ca.key -out ca.crt
