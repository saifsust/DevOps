package com.kubernetes.service.impl;

import com.kubernetes.model.dto.k8s.response.AccessReviewResponse;
import com.kubernetes.model.dto.k8s.response.AccessReviewStatus;
import com.kubernetes.service.AuthorizationReviewer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kubernetes.constants.CommonConstants.ALLOWED;
import static com.kubernetes.constants.CommonConstants.NON_DENIED;

@Slf4j
@Service
public class AuthorizationReviewServiceImpl implements AuthorizationReviewer {

    private List<String> users = List.of(
            "saiful.sust.cse@gmail.com",
            "kubernetes-admin"
    );

    @Override
    public AccessReviewResponse reviewAuthorization(String authorizationRequest) {
        return buildDefaultResponse();
    }

    private AccessReviewResponse buildDefaultResponse() {
        return AccessReviewResponse.builder()
                .apiVersion("authorization.k8s.io/v1beta1")
                .kind("SubjectAccessReview")
                .status(
                        AccessReviewStatus.builder()
                                .allowed(ALLOWED)
                                .denied(NON_DENIED)
                                .build()
                )
                .build();
    }
}
