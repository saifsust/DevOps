package com.kubernetes.model.dto.k8s.selector;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LabelSelector {
    private String rawSelector;
    private List<Requirement> requirements = new ArrayList<>();
}
