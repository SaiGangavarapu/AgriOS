package com.agrios.platform.irrigation.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.irrigation.application.SmartIrrigationAutomationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SmartIrrigationAutomationController {
    private final SmartIrrigationAutomationService service;
    private final TenantContextResolver tenants;

    public SmartIrrigationAutomationController(
            SmartIrrigationAutomationService service,
            TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/irrigation/automation-policies")
    Map<String, UUID> createPolicy(
            @Valid @RequestBody AutomationDtos.PolicyRequest body,
            HttpServletRequest request) {
        return Map.of("policyId", service.createPolicy(
                tenants.resolve(request).tenantId(), actorId(), body));
    }

    @PostMapping("/irrigation/automation-policies/{policyId}/approve")
    void approvePolicy(@PathVariable UUID policyId, HttpServletRequest request) {
        service.approvePolicy(
                tenants.resolve(request).tenantId(), actorId(), policyId);
    }

    @PostMapping("/irrigation/automation-policies/{policyId}/activate")
    void activatePolicy(@PathVariable UUID policyId, HttpServletRequest request) {
        service.activatePolicy(
                tenants.resolve(request).tenantId(), actorId(), policyId);
    }

    @PostMapping("/irrigation/automation-policies/{policyId}/evaluate")
    Map<String, UUID> evaluate(
            @PathVariable UUID policyId,
            @Valid @RequestBody AutomationDtos.EvaluateRequest body,
            HttpServletRequest request) {
        return Map.of("decisionId", service.evaluate(
                tenants.resolve(request).tenantId(), policyId, body));
    }

    @PostMapping("/irrigation/automation-decisions/{decisionId}/approve")
    void approveDecision(@PathVariable UUID decisionId, HttpServletRequest request) {
        service.approveDecision(
                tenants.resolve(request).tenantId(), actorId(), decisionId);
    }

    @PostMapping("/irrigation/automation-decisions/{decisionId}/commands")
    AutomationDtos.CommandResponse command(
            @PathVariable UUID decisionId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            HttpServletRequest request) {
        return service.issueCommand(
                tenants.resolve(request).tenantId(),
                decisionId, idempotencyKey);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
