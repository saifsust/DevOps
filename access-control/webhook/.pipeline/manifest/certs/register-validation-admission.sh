#!/bin/bash
kubectl delete ValidatingWebhookConfiguration pod-creation-validating-admission-conf
kubectl apply -f - <<EOF
apiVersion: admissionregistration.k8s.io/v1
kind: ValidatingWebhookConfiguration
metadata:
  name: pod-creation-validating-admission-conf
webhooks:
- name: 192.168.0.30
  rules:
  - apiGroups:   [""]
    apiVersions: ["v1"]
    operations:  ["CREATE"]
    resources:   ["pods"]
    scope:       "Namespaced"
  clientConfig:
     url: "https://192.168.0.30:30733/validate"
     caBundle: $(cat ca.crt | base64 -w 0)
  admissionReviewVersions: ["v1"]
  sideEffects: None
  timeoutSeconds: 5
EOF