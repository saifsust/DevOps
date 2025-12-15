#!/bin/bash

cat <<EOF > docker-remote-server-credential.yaml
apiVersion: v1
kind: Secret
metadata:
 name: {{ .Values.dockerCertSecret.name }}
 namespace: {{ .Values.appNamespace }}
data:
  {{ .Values.dockerCertSecret.dockerServerCert }}: $(cat /home/saiful-islam/.docker/cert.pem | base64 -w 0)
  {{ .Values.dockerCertSecret.dockerServerKey }}: $(cat /home/saiful-islam/.docker/key.pem | base64 -w 0)
  {{ .Values.dockerCertSecret.dockerCaCert }}: $(cat /home/saiful-islam/.docker/ca.pem | base64 -w 0)