package com.kubernetes.service;

import com.kubernetes.model.dto.K8sWebhookResponse;

public interface WebhookService {

    K8sWebhookResponse processor(String webhookRequest);
}
