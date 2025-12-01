package com.kubernetes.model.dto.k8s;

import lombok.Data;

@Data
public class ReviewTemplateDto {
    private Metadata metadata;
    private SpecDto spec;
}