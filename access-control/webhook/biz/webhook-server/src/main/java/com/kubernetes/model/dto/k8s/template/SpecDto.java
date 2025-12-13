package com.kubernetes.model.dto.k8s.template;

import com.kubernetes.model.dto.k8s.container.ContainerDto;
import lombok.Data;

import java.util.List;

@Data
public class SpecDto {
    private List<ContainerDto> containers;
}