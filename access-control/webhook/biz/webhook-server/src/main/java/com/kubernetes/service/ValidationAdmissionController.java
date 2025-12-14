package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.response.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.request.AdmissionReviewRequestDto;

public interface ValidationAdmissionController {
    AdmissionControllerResponse validate(AdmissionReviewRequestDto admissionReviewRequest);
}
