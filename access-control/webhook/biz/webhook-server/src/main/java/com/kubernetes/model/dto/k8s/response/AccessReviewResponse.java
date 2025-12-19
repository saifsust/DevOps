package com.kubernetes.model.dto.k8s.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AccessReviewResponse {
    private String apiVersion;
    private String kind;
    private AccessReviewStatus status;
}
