package com.kubernetes.service.impl;

import com.kubernetes.model.dto.enums.PatchType;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.request.PatchOperation;
import com.kubernetes.model.dto.k8s.request.PatchRequest;
import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.response.ResponseStatus;
import com.kubernetes.service.MutatingAdmissionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        AdmissionControllerResponse response = defaultResponse(admissionReviewRequest);

        if (!admissionReviewRequest.getRequest().getObject().getMetadata().getLabels().containsKey(REQUIRED_LABEL)) {
            response.getResponse().getWarnings().add(
                    "Missing labels default graphql-system namespace labels"
            );
            PatchRequest patchRequest = PatchRequest.builder()
                    .add(PatchRequest.Request.builder()
                            .op(PatchOperation.ADD)
                            .path(String.format("/metadata/labels/%s", REQUIRED_LABEL))
                            .value(String.format("%s", admissionReviewRequest.getRequest().getObject().getMetadata().getNamespace()))
                            .build()
                    );
            log.info("Patch Request: {}", patchRequest.toJson());
            return response.toBuilder()
                    .response(
                            response.getResponse().toBuilder()
                                    .allowed(ALLOWED)
                                    .patchType(PatchType.JSONPatch)
                                    .patch(patchRequest.toBase64Encoded())
                                    .status(ResponseStatus.builder()
                                            .code(OK)
                                            .message(String.format("Successfully mutate the request for %s", admissionReviewRequest.getRequest().getKind().getKind()))
                                            .build())
                                    .build()
                    )
                    .build();
        }

        return response.toBuilder()
                .response(
                        response.getResponse().toBuilder()
                                .allowed(ALLOWED)
                                .build()
                )
                .build();
    }
}
