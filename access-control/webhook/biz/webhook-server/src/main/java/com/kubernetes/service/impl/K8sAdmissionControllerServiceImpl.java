package com.kubernetes.service.impl;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.AdmissionReviewResponse;
import com.kubernetes.model.dto.k8s.ContainerDto;
import com.kubernetes.service.K8sAdmissionControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kubernetes.constants.CommonConstants.ADMISSION_REVIEW_API_VERSION;
import static com.kubernetes.constants.CommonConstants.ADMISSION_REVIEW_KIND;
import static com.kubernetes.constants.CommonConstants.ALLOWED;
import static com.kubernetes.constants.CommonConstants.ALLOWED_REGISTRIES;

@Slf4j
@Service
public class K8sAdmissionControllerServiceImpl implements K8sAdmissionControllerService {

    @Override
    public AdmissionControllerResponse processor(AdmissionReviewRequestDto admissionReviewRequest) {
        log.info("k8s review request: {}", admissionReviewRequest);
        AdmissionControllerResponse response = buildResponse(admissionReviewRequest);

        if (haveAllImagesValidRegistry(admissionReviewRequest)) {
            return response.toBuilder()
                    .response(
                            response.getResponse().toBuilder()
                                    .allowed(ALLOWED)
                                    .build()
                    )
                    .build();
        }

        return response;
    }

    private AdmissionControllerResponse buildResponse(AdmissionReviewRequestDto admissionReviewRequest) {
        return AdmissionControllerResponse.builder()
                .apiVersion(ADMISSION_REVIEW_API_VERSION)
                .kind(ADMISSION_REVIEW_KIND)
                .response(
                        AdmissionReviewResponse.builder()
                                .uid(admissionReviewRequest.getRequest().getUid())
                                .build()
                )
                .build();
    }

    private boolean haveAllImagesValidRegistry(AdmissionReviewRequestDto reviewRequest) {
        return reviewRequest.getRequest()
                .getObject()
                .getSpec()
                .getContainers()
                .stream()
                .allMatch(this::isAllowedRegistry);
    }

    private boolean isAllowedRegistry(ContainerDto container) {
        return ALLOWED_REGISTRIES
                .stream()
                .anyMatch(container.getImage()::startsWith);
    }
}