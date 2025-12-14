package com.kubernetes.model.dto.k8s.request;

import com.kubernetes.model.dto.k8s.container.ContainerDto;
import com.kubernetes.model.dto.k8s.kind.KindDto;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kubernetes.constants.CommonConstants.DEFAULT_NAMESPACE;

@Data
public class AdmissionReviewRequestDto {
    private String apiVersion;
    private String kind;
    private ReviewRequestDto request;

    public String getNamespace() {
        if (Objects.isNull(this.request)) {
            return DEFAULT_NAMESPACE;
        }
        return this.request.getObject().getMetadata().getNamespace();
    }

    public KindDto getKindDto() {
        return Objects.requireNonNull(this.request)
                .getKind();
    }

    public Map<String, Object> getLabels() {
        return Objects.requireNonNull(this.request)
                .getObject()
                .getMetadata()
                .getLabels();
    }

    public List<ContainerDto> getContainers() {
        return Objects.requireNonNull(this.request)
                .getObject()
                .getSpec()
                .getContainers();
    }
}