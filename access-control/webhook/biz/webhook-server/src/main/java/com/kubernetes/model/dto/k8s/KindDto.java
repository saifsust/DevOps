package com.kubernetes.model.dto.k8s;

import com.kubernetes.model.dto.enums.Kind;
import lombok.Data;

@Data
public class KindDto {
    private String group;
    private String version;
    private Kind kind;
}