package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.AdmissionReviewRequestDto;

public interface K8sAdmissionControllerService {

    AdmissionControllerResponse processor(AdmissionReviewRequestDto admissionReviewRequest);
}
