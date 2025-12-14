package com.kubernetes.model.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePullPolicy {
    Always,
    IfNotPresent,
    Never
}
