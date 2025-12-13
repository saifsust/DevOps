package com.kubernetes.model.dto.k8s.request;

import lombok.Data;

@Data
public class PatchRequest {
    private PatchOperation op;
    private String path;
    private Object value;
}
