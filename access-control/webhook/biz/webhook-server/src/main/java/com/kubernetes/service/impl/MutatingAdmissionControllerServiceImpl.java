package com.kubernetes.service.impl;

import com.kubernetes.model.dto.enums.ImagePullPolicy;
import com.kubernetes.model.dto.enums.PatchOperation;
import com.kubernetes.model.dto.enums.PatchType;
import com.kubernetes.model.dto.k8s.container.ContainerDto;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.request.PatchRequest;
import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.response.ResponseStatus;
import com.kubernetes.service.MutatingAdmissionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kubernetes.constants.CommonConstants.ALLOWED;
import static com.kubernetes.constants.CommonConstants.OK;
import static com.kubernetes.constants.CommonConstants.REQUIRED_LABEL;
import static com.kubernetes.utils.CommonUtils.defaultResponse;

@Slf4j
@Service
public class MutatingAdmissionControllerServiceImpl implements MutatingAdmissionController {

    @Override
    public AdmissionControllerResponse mutate(AdmissionReviewRequestDto admissionReviewRequest) {
        log.info("Mutation request: {}", admissionReviewRequest);
        AdmissionControllerResponse admissionDefaultResponse = defaultResponse(
                admissionReviewRequest
        );

        PatchRequest patchRequest = getPatchRequest(
                admissionReviewRequest
        );

        if (hasNoRequiredLabels(admissionReviewRequest)) {
            addWarning(
                    admissionDefaultResponse
            );
        }

        if (hasPatchRequest(patchRequest)) {
            log.info("Patch Request: {}", patchRequest.toJson());
            return getMutationSuccessResponse(
                    admissionReviewRequest,
                    admissionDefaultResponse,
                    patchRequest
            );
        }

        return getMutationDefaultResponse(
                admissionDefaultResponse
        );
    }

    private AdmissionControllerResponse getMutationSuccessResponse(
            AdmissionReviewRequestDto admissionReviewRequest,
            AdmissionControllerResponse response,
            PatchRequest patchRequest
    ) {
        return response.toBuilder()
                .response(
                        response.getResponse().toBuilder()
                                .allowed(ALLOWED)
                                .patchType(PatchType.JSONPatch)
                                .patch(patchRequest.toBase64Encoded())
                                .status(ResponseStatus.builder()
                                        .code(OK)
                                        .message(
                                                String.format("Successfully mutate the request for %s", admissionReviewRequest.getRequest().getKind().getKind())
                                        )
                                        .build())
                                .build()
                )
                .build();
    }

    private AdmissionControllerResponse getMutationDefaultResponse(AdmissionControllerResponse response) {
        return response.toBuilder()
                .response(
                        response.getResponse().toBuilder()
                                .allowed(ALLOWED)
                                .build()
                )
                .build();
    }

    private void addWarning(AdmissionControllerResponse response) {
        response.getResponse().getWarnings().add(
                "Missing required identity labels"
        );
    }

    private boolean hasPatchRequest(PatchRequest patchRequest) {
        return !CollectionUtils.isEmpty(
                patchRequest.getRequests()
        );
    }

    private PatchRequest getPatchRequest(AdmissionReviewRequestDto admissionReviewRequest) {
        return PatchRequest.builder()
                .addAll(
                        getPatchRequests(admissionReviewRequest)
                );
    }

    private List<PatchRequest.Request> getPatchRequests(AdmissionReviewRequestDto admissionReviewRequest) {
        List<PatchRequest.Request> requests = new ArrayList<>();
        if (hasNoRequiredLabels(admissionReviewRequest)) {
            requests.add(
                    getMutatedLabelsRequest(
                            admissionReviewRequest
                    )
            );
        }

        if (hasNoAlwaysImagePullPolicy(admissionReviewRequest)) {
            requests.addAll(
                    getMutatedImagePullPolicyRequests(
                            admissionReviewRequest
                    )
            );
        }

        return requests;
    }

    private PatchRequest.Request getMutatedLabelsRequest(AdmissionReviewRequestDto admissionReviewRequest) {
        return PatchRequest.Request.builder()
                .op(PatchOperation.ADD)
                .path(String.format("/metadata/labels/%s", REQUIRED_LABEL))
                .value(
                        String.format("%s", admissionReviewRequest.getRequest().getObject().getMetadata().getNamespace())
                )
                .build();
    }

    private List<PatchRequest.Request> getMutatedImagePullPolicyRequests(AdmissionReviewRequestDto admissionReviewRequest) {
        List<PatchRequest.Request> requests = new ArrayList<>();

        List<ContainerDto> containers = getContainers(admissionReviewRequest);
        for (int containerIdx = 0; containerIdx < containers.size(); containerIdx++) {
            if (nonAlwaysImagePullPolicy(containers.get(containerIdx))) {
                requests.add(
                        PatchRequest.Request.builder()
                                .op(PatchOperation.ADD)
                                .path(String.format("/spec/containers/%d/imagePullPolicy", containerIdx))
                                .value(ImagePullPolicy.Always)
                                .build()
                );
            }
        }
        return requests;
    }

    private boolean hasNoAlwaysImagePullPolicy(AdmissionReviewRequestDto admissionReviewRequest) {
        return getContainers(admissionReviewRequest)
                .stream()
                .allMatch(this::nonAlwaysImagePullPolicy);
    }

    private boolean nonAlwaysImagePullPolicy(ContainerDto container) {
        return !container.getImagePullPolicy()
                .equals(
                        ImagePullPolicy.Always
                );
    }

    private List<ContainerDto> getContainers(AdmissionReviewRequestDto admissionReviewRequest) {
        return admissionReviewRequest
                .getRequest()
                .getObject()
                .getSpec()
                .getContainers();
    }

    private boolean hasNoRequiredLabels(AdmissionReviewRequestDto admissionReviewRequest) {
        return !admissionReviewRequest
                .getRequest()
                .getObject()
                .getMetadata()
                .getLabels()
                .containsKey(REQUIRED_LABEL);
    }
}
