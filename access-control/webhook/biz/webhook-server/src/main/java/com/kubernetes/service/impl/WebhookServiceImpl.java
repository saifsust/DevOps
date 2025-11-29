package com.kubernetes.service.impl;

import com.kubernetes.model.dto.K8sResponse;
import com.kubernetes.model.dto.K8sWebhookResponse;
import com.kubernetes.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class WebhookServiceImpl implements WebhookService {

    @Override
    public K8sWebhookResponse processor(String webhookRequest) {
        log.info("k8s request received: {}", webhookRequest);
        return K8sWebhookResponse.builder()
                .apiVersion("admission.k8s.io/v1")
                .kind("AdmissionReview")
                .response(
                        K8sResponse.builder()
                                .uid(UUID.randomUUID().toString())
                                .allowed(true)
                                .build()
                )
                .build();
    }
}