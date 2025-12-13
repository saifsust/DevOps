package com.kubernetes.model.dto.k8s.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PatchOperation {
    ADD("add"),
    REMOVE("remove"),
    REPLACE("replace");

    private final String value;
}
