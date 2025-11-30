package com.kubernetes.service;

import com.kubernetes.model.dto.K8sAdmissionControllerResponse;

public interface K8sAdmissionControllerService {

    K8sAdmissionControllerResponse processor(String webhookRequest);
}
