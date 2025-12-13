package com.kubernetes.service.impl;

import com.kubernetes.constants.CommonConstants;
import com.kubernetes.model.dto.enums.Kind;
import com.kubernetes.model.dto.k8s.container.ContainerDto;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.response.ResponseStatus;
import com.kubernetes.service.ValidationAdmissionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kubernetes.constants.CommonConstants.ALLOWED;
import static com.kubernetes.constants.CommonConstants.ALLOWED_REGISTRIES;
import static com.kubernetes.utils.CommonUtils.defaultResponse;

@Slf4j
@Service
public class ValidationAdmissionControllerServiceImpl implements ValidationAdmissionController {

    @Override
    public AdmissionControllerResponse validate(AdmissionReviewRequestDto admissionReviewRequest) {
        log.info("k8s review request: {}", admissionReviewRequest);
        AdmissionControllerResponse response = defaultResponse(admissionReviewRequest);

        if (isAllowedRegistry(admissionReviewRequest)) {
            return response.toBuilder()
                    .response(
                            response.getResponse()
                                    .toBuilder()
                                    .allowed(ALLOWED)
                                    .status(
                                            ResponseStatus.builder()
                                                    .code(CommonConstants.OK)
                                                    .message(
                                                            String.format("Successfully deploy the resource %s", admissionReviewRequest.getRequest().getKind().getKind())
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
        }

        return response;
    }

    private boolean isAllowedRegistry(AdmissionReviewRequestDto reviewRequest) {
        return isPodDeploymentRequest(reviewRequest) &&
                haveAllImagesValidRegistry(reviewRequest);
    }

    private boolean isPodDeploymentRequest(AdmissionReviewRequestDto reviewRequest) {
        return reviewRequest.getRequest()
                .getKind()
                .getKind()
                .equals(Kind.Pod);
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