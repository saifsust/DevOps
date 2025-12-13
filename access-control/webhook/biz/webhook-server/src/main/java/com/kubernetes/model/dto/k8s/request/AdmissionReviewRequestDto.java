package com.kubernetes.model.dto.k8s.request;

import lombok.Data;

@Data
public class AdmissionReviewRequestDto {
    private String apiVersion;
    private String kind;
    private ReviewRequestDto request;
}