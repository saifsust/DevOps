package com.kubernetes.model.dto.k8s.response;

import com.kubernetes.model.dto.enums.PatchType;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class AdmissionReviewResponse {
    @Builder.Default
    private boolean allowed = false;
    private String uid;
    private ResponseStatus status;
    @Builder.Default
    private List<String> warnings = new ArrayList<>();
    private PatchType patchType;
    private String patch;
}