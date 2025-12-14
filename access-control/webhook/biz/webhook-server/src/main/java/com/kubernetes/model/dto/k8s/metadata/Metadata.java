package com.kubernetes.model.dto.k8s.metadata;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Metadata {
    private String uid;
    private String name;
    private String namespace;
    private Map<String, Object> labels = new HashMap<>();
}