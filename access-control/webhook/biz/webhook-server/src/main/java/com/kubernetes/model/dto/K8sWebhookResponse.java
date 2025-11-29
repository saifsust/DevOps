package com.kubernetes.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class K8sWebhookResponse {
    private String apiVersion;
    private String kind;
    private K8sResponse response;
}