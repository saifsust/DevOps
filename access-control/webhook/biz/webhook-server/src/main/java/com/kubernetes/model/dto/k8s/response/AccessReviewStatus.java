package com.kubernetes.model.dto.k8s.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AccessReviewStatus {
    @Builder.Default
    private boolean allowed = false;
    @Builder.Default
    private boolean denied = true;
    //optional: why was allowed or denied
    private String reason;
    // Some errors occurred during authorization check
    private String evaluationError;
}
