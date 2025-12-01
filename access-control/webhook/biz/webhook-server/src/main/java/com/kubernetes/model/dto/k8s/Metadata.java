package com.kubernetes.model.dto.k8s;

import lombok.Data;

@Data
public class Metadata {
    private String uid;
    private String name;
    private String namespace;
}