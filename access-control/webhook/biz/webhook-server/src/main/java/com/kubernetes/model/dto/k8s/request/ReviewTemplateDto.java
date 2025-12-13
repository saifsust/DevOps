package com.kubernetes.model.dto.k8s.request;

import com.kubernetes.model.dto.k8s.metadata.Metadata;
import com.kubernetes.model.dto.k8s.template.SpecDto;
import lombok.Data;

@Data
public class ReviewTemplateDto {
    private Metadata metadata;
    private SpecDto spec;
}