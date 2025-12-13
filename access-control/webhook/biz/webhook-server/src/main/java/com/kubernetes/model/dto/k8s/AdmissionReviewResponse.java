package com.kubernetes.model.dto.k8s;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AdmissionReviewResponse {
    private boolean allowed;
    private String uid;
    private ResponseStatus status;
}