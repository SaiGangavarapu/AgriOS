package com.agrios.platform.harvest.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.harvest.application.HarvestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HarvestController {
    private final HarvestService service;
    private final TenantContextResolver tenants;

    public HarvestController(HarvestService service,
                             TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/harvest/plans")
    HarvestDtos.PlanResponse createPlan(
            @Valid @RequestBody HarvestDtos.PlanRequest body,
            HttpServletRequest request) {
        return service.createPlan(
                tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/harvest/plans/{planId}/approve")
    HarvestDtos.PlanResponse approve(
            @PathVariable UUID planId, HttpServletRequest request) {
        return service.approve(
                tenants.resolve(request).tenantId(), actorId(), planId);
    }

    @PostMapping("/harvest/plans/{planId}/batches")
    HarvestDtos.BatchResponse createBatch(
            @PathVariable UUID planId,
            @Valid @RequestBody HarvestDtos.BatchRequest body,
            HttpServletRequest request) {
        return service.createBatch(
                tenants.resolve(request).tenantId(), actorId(), planId, body);
    }

    @GetMapping("/crop-cycles/{cycleId}/harvest-batches")
    List<HarvestDtos.BatchResponse> list(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.listBatches(
                tenants.resolve(request).tenantId(), cycleId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
