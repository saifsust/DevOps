package com.kubernetes.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class K8sAdmissionReviewResponse {
    private boolean allowed;
    private String uid;
}