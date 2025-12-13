package com.kubernetes.utils;

import com.kubernetes.constants.CommonConstants;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;
import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.response.AdmissionReviewResponse;
import com.kubernetes.model.dto.k8s.response.ResponseStatus;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.kubernetes.constants.CommonConstants.ADMISSION_REVIEW_API_VERSION;
import static com.kubernetes.constants.CommonConstants.ADMISSION_REVIEW_KIND;
import static com.kubernetes.constants.CommonConstants.DISALLOWED;

@UtilityClass
public class CommonUtils {

    public static AdmissionControllerResponse defaultResponse(AdmissionReviewRequestDto admissionReviewRequest) {
        return AdmissionControllerResponse.builder()
                .apiVersion(ADMISSION_REVIEW_API_VERSION)
                .kind(ADMISSION_REVIEW_KIND)
                .response(
                        AdmissionReviewResponse.builder()
                                .uid(admissionReviewRequest.getRequest().getUid())
                                .allowed(DISALLOWED)
                                .status(
                                        ResponseStatus
                                                .builder()
                                                .code(CommonConstants.BAD_REQUEST)
                                                .message(
                                                        String.format("You can not deploy the resource %s", admissionReviewRequest.getRequest().getKind().getKind())
                                                )
                                                .build()

                                )
                                .build()
                )
                .build();
    }

    public static String mapToBase64EncodedStr(String plainStr) {
        return new String(
                Base64.getEncoder().encode(
                        plainStr.getBytes(StandardCharsets.UTF_8)
                ),
                StandardCharsets.UTF_8
        );
    }
}
