package com.kubernetes.service;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;

public interface MutatingAdmissionController {
    AdmissionControllerResponse mutate(String request);
}
