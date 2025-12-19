package com.kubernetes.model.dto.k8s.template;

import com.kubernetes.model.dto.k8s.resource.ResourceAttributes;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SubjectAccessReviewSpec {
    private ResourceAttributes resourceAttributes;
    private String user;
    private Map<String, String> extra = new HashMap<>();
    private List<String> groups = new ArrayList<>();
}
