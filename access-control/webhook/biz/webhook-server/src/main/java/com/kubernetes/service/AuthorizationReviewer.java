package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.request.SubjectAccessReviewRequest;
import com.kubernetes.model.dto.k8s.response.AccessReviewResponse;

public interface AuthorizationReviewer {
    AccessReviewResponse reviewAuthorization(SubjectAccessReviewRequest authorizationRequest);
}
