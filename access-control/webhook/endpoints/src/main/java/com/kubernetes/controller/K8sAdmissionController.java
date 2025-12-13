package com.kubernetes.controller;

import com.kubernetes.model.dto.k8s.AdmissionControllerResponse;
import com.kubernetes.model.dto.k8s.AdmissionReviewRequestDto;
import com.kubernetes.service.MutatingAdmissionController;
import com.kubernetes.service.ValidationAdmissionController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kubernetes.controller.K8sAdmissionController.API_PATH;

@Slf4j
@RestController
@RequestMapping(path = API_PATH)
@RequiredArgsConstructor
public class K8sAdmissionController {
    static final String API_PATH = "/";
    private final ValidationAdmissionController validationAdmissionController;
    private final MutatingAdmissionController mutatingAdmissionController;

    @PostMapping(path = "validate")
    public ResponseEntity<@NonNull AdmissionControllerResponse> validateAdmissionReview(
            @RequestBody AdmissionReviewRequestDto admissionReviewRequest,
            HttpServletRequest request
    ) {
        log.info("Receive request ....");
        return ResponseEntity
                .ok(validationAdmissionController.validate(admissionReviewRequest));
    }

    @PostMapping(path = "mutate")
    public ResponseEntity<@NonNull AdmissionControllerResponse> mutateK8sObject(
            @RequestBody String admissionReviewRequestDto,
            HttpServletRequest request
    ) {
        log.info("K8s Object is mutated");
        return ResponseEntity
                .ok(mutatingAdmissionController.mutate(admissionReviewRequestDto));
    }

    @GetMapping(path = "health")
    public ResponseEntity<@NonNull Void> healthChecker(HttpServletRequest request) {
        //log.info("Health check request receiving.....");
        return ResponseEntity.ok()
                .build();
    }
}
