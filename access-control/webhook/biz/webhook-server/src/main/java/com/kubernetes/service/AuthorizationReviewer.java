package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.response.AccessReviewResponse;

public interface AuthorizationReviewer {
    AccessReviewResponse reviewAuthorization(String authorizationRequest);
}
