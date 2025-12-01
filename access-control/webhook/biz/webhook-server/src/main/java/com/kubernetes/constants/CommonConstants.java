package com.kubernetes.constants;

import java.util.List;

public class CommonConstants {
    public static final boolean ALLOWED = true;
    private static final boolean DISALLOWED = false;

    public static final String ADMISSION_REVIEW_API_VERSION = "admission.k8s.io/v1";
    public static final String ADMISSION_REVIEW_KIND = "AdmissionReview";

    public static final List<String> ALLOWED_REGISTRIES = List.of(
      "saifsust/"
    );
}
