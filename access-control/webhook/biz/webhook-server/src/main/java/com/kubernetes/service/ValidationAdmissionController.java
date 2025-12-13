package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.AdmissionReviewRequestDto;

public interface ValidationAdmissionController {

    AdmissionControllerResponse validate(AdmissionReviewRequestDto admissionReviewRequest);
}
