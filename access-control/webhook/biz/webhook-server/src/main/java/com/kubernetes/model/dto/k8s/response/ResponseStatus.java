package com.kubernetes.model.dto.k8s.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseStatus {
    private int code;
    private String message;
}
