package com.agrios.platform.crophealth.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.crophealth.application.IpmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IpmController {
    private final IpmService service;
    private final TenantContextResolver tenants;

    public IpmController(IpmService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/ipm/plans")
    IpmDtos.PlanResponse generate(
            @Valid @RequestBody IpmDtos.GenerateRequest body,
            HttpServletRequest request) {
        return service.generate(tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/ipm/plans/{planId}/approve")
    IpmDtos.PlanResponse approve(
            @PathVariable UUID planId,
            @Valid @RequestBody IpmDtos.ApproveRequest body,
            HttpServletRequest request) {
        return service.approve(
                tenants.resolve(request).tenantId(), actorId(), planId, body);
    }

    @PostMapping("/ipm/plans/{planId}/executions")
    Map<String, UUID> execute(
            @PathVariable UUID planId,
            @Valid @RequestBody IpmDtos.ExecuteRequest body,
            HttpServletRequest request) {
        return Map.of("executionId", service.execute(
                tenants.resolve(request).tenantId(), actorId(), planId, body));
    }

    @GetMapping("/crop-cycles/{cycleId}/ipm-plans")
    List<IpmDtos.PlanResponse> list(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.list(tenants.resolve(request).tenantId(), cycleId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
