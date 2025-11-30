package com.kubernetes.service.impl;

import com.kubernetes.model.dto.K8sAdmissionReviewResponse;
import com.kubernetes.model.dto.K8sAdmissionControllerResponse;
import com.kubernetes.service.K8sAdmissionControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class K8sAdmissionControllerServiceImpl implements K8sAdmissionControllerService {

    @Override
    public K8sAdmissionControllerResponse processor(String admissionReviewRequest) {
        log.info("k8s request received: {}", admissionReviewRequest);
        return K8sAdmissionControllerResponse.builder()
                .apiVersion("admission.k8s.io/v1")
                .kind("AdmissionReview")
                .response(
                        K8sAdmissionReviewResponse.builder()
                                .uid(UUID.randomUUID().toString())
                                .allowed(true)
                                .build()
                )
                .build();
    }
}