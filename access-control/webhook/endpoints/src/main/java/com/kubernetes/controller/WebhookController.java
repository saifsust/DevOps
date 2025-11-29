package com.kubernetes.controller;

import com.kubernetes.model.dto.K8sWebhookResponse;
import com.kubernetes.service.WebhookService;
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

import static com.kubernetes.controller.WebhookController.API_PATH;

@Slf4j
@RestController
@RequestMapping(path = API_PATH)
@RequiredArgsConstructor
public class WebhookController {
    static final String API_PATH = "/";
    private final WebhookService webhookService;

    @PostMapping(path = "validate")
    public ResponseEntity<@NonNull K8sWebhookResponse> webhook(
            @RequestBody String webhookRequest,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .ok(webhookService.processor(webhookRequest));
    }

    @GetMapping(path = "health")
    public ResponseEntity<@NonNull Void> healthChecker() {
        log.info("Health check request receiving.....");
        return ResponseEntity.ok()
                .build();
    }
}
