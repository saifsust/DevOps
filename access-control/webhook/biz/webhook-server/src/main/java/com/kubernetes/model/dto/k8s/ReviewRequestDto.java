package com.kubernetes.model.dto.k8s;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private String uid;
    private KindDto kind;
    private ReviewTemplateDto object;
}