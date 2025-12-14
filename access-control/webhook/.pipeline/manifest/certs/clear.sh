#!/bin/bash
kubectl delete MutatingWebhookConfiguration pod-creation-mutation-admission-conf
kubectl delete ValidatingWebhookConfiguration pod-creation-validating-admission-conf
kubectl delete secret webhook-credentials --namespace=istio-system

kubectl create namespace webhook-system
kubectl label namespace webhook-system istio-injection=enabled
