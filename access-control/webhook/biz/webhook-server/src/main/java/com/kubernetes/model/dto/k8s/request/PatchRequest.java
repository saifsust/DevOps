package com.kubernetes.model.dto.k8s.request;

import lombok.Builder;
import lombok.Getter;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static com.kubernetes.utils.CommonUtils.mapToBase64EncodedStr;


@Getter
public class PatchRequest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    List<Request> requests = new ArrayList<>();

    public static PatchRequest builder() {
        return new PatchRequest();
    }

    public String toJson() {
        return MAPPER.
                writeValueAsString(requests);
    }

    public String toBase64Encoded() {
        return mapToBase64EncodedStr(
                this.toJson()
        );
    }

    public PatchRequest add(Request request) {
        this.requests.add(request);
        return this;
    }

    public PatchRequest addAll(List<Request> requests) {
        this.requests.addAll(requests);
        return this;
    }

    @Getter
    @Builder
    public static class Request {
        private PatchOperation op;
        private String path;
        private Object value;

        public String getOp() {
            return this.op.getValue();
        }
    }
}
