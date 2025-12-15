#!/bin/bash
cat <<EOF > k8s-admin-credentials-secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: {{ .Values.k8sAdminCredentialsSecret.name }}
  namespace: {{ .Values.appNamespace }}
data:
  {{ .Values.k8sAdminCredentialsSecret.k8sAdminCert }}: $(kubectl config view --raw -ojsonpath="{.users[0].user.client-certificate-data}")
  {{ .Values.k8sAdminCredentialsSecret.k8sAdminKey }}: $(kubectl config view --raw -ojsonpath="{.users[0].user.client-key-data}")
EOF