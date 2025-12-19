package com.kubernetes.controller;

import com.kubernetes.model.dto.k8s.request.SubjectAccessReviewRequest;
import com.kubernetes.model.dto.k8s.response.AccessReviewResponse;
import com.kubernetes.service.AuthorizationReviewer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class k8sAuthorizationController {
    private final AuthorizationReviewer authorizationReviewer;

    @PostMapping(path = "/authorize")
    public ResponseEntity<@NonNull AccessReviewResponse> authorize(
            @RequestBody SubjectAccessReviewRequest authorizationRequest,
            HttpServletRequest request
    ) {
        log.info("{}", authorizationRequest);
        return ResponseEntity.ok(
                authorizationReviewer.reviewAuthorization(authorizationRequest)
        );
    }
}
