package com.kubernetes.model.dto.k8s.selector;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Requirement {
    private String key;
    private String operator;
    private List<String> values = new ArrayList<>();
}
