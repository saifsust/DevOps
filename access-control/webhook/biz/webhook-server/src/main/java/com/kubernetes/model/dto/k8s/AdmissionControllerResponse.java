package com.kubernetes.model.dto.k8s;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AdmissionControllerResponse {
    private String apiVersion;
    private String kind;
    private AdmissionReviewResponse response;
}