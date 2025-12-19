package com.kubernetes.service.impl;

import com.kubernetes.model.dto.k8s.request.SubjectAccessReviewRequest;
import com.kubernetes.model.dto.k8s.response.AccessReviewResponse;
import com.kubernetes.model.dto.k8s.response.AccessReviewStatus;
import com.kubernetes.service.AuthorizationReviewer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kubernetes.constants.CommonConstants.ALLOWED;
import static com.kubernetes.constants.CommonConstants.DENIED;
import static com.kubernetes.constants.CommonConstants.DISALLOWED;
import static com.kubernetes.constants.CommonConstants.NON_DENIED;

@Slf4j
@Service
public class AuthorizationReviewServiceImpl implements AuthorizationReviewer {

    private List<String> users = List.of(
            "saiful.sust.cse@gmail.com",
            "kubernetes-admin"
    );

    @Override
    public AccessReviewResponse reviewAuthorization(SubjectAccessReviewRequest authorizationRequest) {
        AccessReviewResponse response = buildDefaultResponse(authorizationRequest);

        if (!users.contains(authorizationRequest.getSpec().getUser())) {
            return response.toBuilder()
                    .status(response.getStatus().toBuilder()
                            .reason(
                                    String.format(
                                            "User has no permission to %s resource %s",
                                            authorizationRequest.getSpec().getResourceAttributes().getVerb(),
                                            authorizationRequest.getSpec().getResourceAttributes().getResource()
                                    )
                            )
                            .allowed(DISALLOWED)
                            .denied(DENIED)
                            .build())
                    .build();
        }
        return response;
    }

    private AccessReviewResponse buildDefaultResponse(
            SubjectAccessReviewRequest authorizationRequest
    ) {
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
