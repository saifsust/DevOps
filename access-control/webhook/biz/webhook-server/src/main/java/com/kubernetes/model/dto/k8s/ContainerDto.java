package com.kubernetes.model.dto.k8s;

import lombok.Data;

@Data
public class ContainerDto {
    private String name;
    private String image;
    private String imagePullPolicy;
}