#!/bin/bash

configmap="authenticator-webhook.yaml"

cat <<EOF > $configmap
apiVersion: v1
kind: Config
clusters:
- name: authenticator-webhook
  cluster:
    certificate-authority-data: $(cat ca.crt | base64 -w 0)
    server: https://$(cat host):$(cat port)/authorize
contexts:
- name: authenticator@authenticator-webhook
  context:
    cluster: authenticator-webhook
    user: authenticator
users:
- name: authenticator
  user:
    client-certificate-data: $(cat client.crt | base64 -w 0)
    client-key-data: $(cat client.key | base64 -w 0)
current-context: authenticator@authenticator-webhook
EOF
