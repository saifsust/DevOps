package com.kubernetes.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class K8sResponse {
    private boolean allowed;
    private String uid;
}