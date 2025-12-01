package com.kubernetes.model.dto.k8s;

import lombok.Data;

import java.util.List;

@Data
public class SpecDto {
    private List<ContainerDto> containers;
}