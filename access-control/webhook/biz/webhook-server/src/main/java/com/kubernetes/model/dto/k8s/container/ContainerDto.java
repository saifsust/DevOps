package com.kubernetes.model.dto.k8s.container;

import com.kubernetes.model.dto.enums.ImagePullPolicy;
import lombok.Data;

@Data
public class ContainerDto {
    private String name;
    private String image;
    private ImagePullPolicy imagePullPolicy;
}