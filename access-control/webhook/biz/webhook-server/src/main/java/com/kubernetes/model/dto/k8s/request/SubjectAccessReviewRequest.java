package com.kubernetes.model.dto.k8s.request;

import com.kubernetes.model.dto.k8s.metadata.Metadata;
import com.kubernetes.model.dto.k8s.template.SubjectAccessReviewSpec;
import lombok.Data;

@Data
public class SubjectAccessReviewRequest {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private SubjectAccessReviewSpec spec;
}
