#!/bin/bash
kubectl delete ValidatingWebhookConfiguration pod-creation-validating-admission-conf
kubectl apply -f - <<EOF
apiVersion: admissionregistration.k8s.io/v1
kind: ValidatingWebhookConfiguration
metadata:
  name: pod-creation-validating-admission-conf
webhooks:
- name: $(cat host)
  rules:
  - apiGroups:   [""]
    apiVersions: ["v1"]
    operations:  ["CREATE"]
    resources:   ["pods"]
    scope:       "Namespaced"
  clientConfig:
     url: "https://$(cat host):$(cat port)/validate"
     caBundle: $(cat ca.crt | base64 -w 0)
  admissionReviewVersions: ["v1"]
  sideEffects: None
  timeoutSeconds: 5
EOF