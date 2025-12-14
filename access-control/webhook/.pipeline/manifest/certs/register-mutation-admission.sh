#!/bin/bash
kubectl delete MutatingWebhookConfiguration pod-creation-mutation-admission-conf
kubectl apply -f - <<EOF
apiVersion: admissionregistration.k8s.io/v1
kind: MutatingWebhookConfiguration
metadata:
  name: pod-creation-mutation-admission-conf
webhooks:
- name: $(cat host)
  rules:
  - apiGroups:   [""]
    apiVersions: ["v1"]
    operations:  ["CREATE"]
    resources:   ["pods"]
    scope:       "Namespaced"
  clientConfig:
     url: "https://$(cat host):$(cat port)/mutate"
     caBundle: $(cat ca.crt | base64 -w 0)
  admissionReviewVersions: ["v1"]
  sideEffects: None
  timeoutSeconds: 5
EOF