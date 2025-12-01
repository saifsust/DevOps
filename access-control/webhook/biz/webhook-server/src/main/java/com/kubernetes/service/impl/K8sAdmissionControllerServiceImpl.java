package com.kubernetes.service.impl;

import com.kubernetes.model.dto.k8s.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.AdmissionReviewResponse;
import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.service.K8sAdmissionControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class K8sAdmissionControllerServiceImpl implements K8sAdmissionControllerService {

    @Override
    public AdmissionControllerResponse processor(AdmissionReviewRequestDto admissionReviewRequest) {
        log.info("k8s review request: {}", admissionReviewRequest.getRequest());
        log.info("Pod name request: {}", admissionReviewRequest.getRequest().getObject().getMetadata().getName());
        log.info("Pod name request: {}", admissionReviewRequest.getRequest().getObject().getSpec().getContainers());

        return AdmissionControllerResponse.builder()
                .apiVersion("admission.k8s.io/v1")
                .kind("AdmissionReview")
                .response(
                        AdmissionReviewResponse.builder()
                                .uid(admissionReviewRequest.getRequest().getUid())
                                .allowed(true)
                                .build()
                )
                .build();
    }
}