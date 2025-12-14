#!/bin/bash
kubectl delete MutatingWebhookConfiguration pod-creation-mutation-admission-conf
kubectl delete ValidatingWebhookConfiguration pod-creation-validating-admission-conf
