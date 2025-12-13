package com.kubernetes.service.impl;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.service.MutatingAdmissionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MutatingAdmissionControllerServiceImpl implements MutatingAdmissionController {

    @Override
    public AdmissionControllerResponse mutate(String request) {
        log.info("Mutation request: {}", request);
        return null;
    }
}
