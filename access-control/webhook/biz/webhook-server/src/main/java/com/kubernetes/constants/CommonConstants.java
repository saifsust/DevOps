package com.kubernetes.constants;

import java.util.List;

public class CommonConstants {
    public static final boolean ALLOWED = true;
    public static final boolean DISALLOWED = false;

    public static final String REQUIRED_LABEL = "k8s-webhook.io~1ns-name";
    public static final String METADATA_LABELS = "/metadata/labels";
    public static final String DEFAULT_NAMESPACE = "default";
    public static final String PATCH_ENCODED_FORWARD_SLASH = "~1";
    public static final String PATCH_DECODED_FORWARD_SLASH = "/";

    public static final Integer OK = 200;
    public static final Integer BAD_REQUEST = 400;

    public static final String ADMISSION_REVIEW_API_VERSION = "admission.k8s.io/v1";
    public static final String ADMISSION_REVIEW_KIND = "AdmissionReview";

    public static final List<String> ALLOWED_REGISTRIES = List.of(
      "saifsust/"
    );
}
