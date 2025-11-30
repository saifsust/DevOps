package com.kubernetes.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class K8sAdmissionControllerResponse {
    private String apiVersion;
    private String kind;
    private K8sAdmissionReviewResponse response;
}