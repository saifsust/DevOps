package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;

public interface MutatingAdmissionController {
    AdmissionControllerResponse mutate(AdmissionReviewRequestDto admissionReviewRequest);
}
