package com.kubernetes.model.dto.k8s.resource;

import com.kubernetes.model.dto.k8s.selector.LabelSelector;
import lombok.Data;

@Data
public class ResourceAttributes {
    private String name;
    private String namespace;
    private String verb;
    private String group;
    private String resource;
    private LabelSelector fieldSelector;
    private LabelSelector labelSelector;
    private String uid;
    private String user;
    private String version;
}
