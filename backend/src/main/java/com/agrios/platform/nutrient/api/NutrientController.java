package com.agrios.platform.nutrient.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.nutrient.application.NutrientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class NutrientController {
    private final NutrientService service;
    private final TenantContextResolver tenants;

    public NutrientController(NutrientService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/nutrient-plans/generate")
    NutrientDtos.PlanResponse generate(
            @Valid @RequestBody NutrientDtos.GeneratePlanRequest body,
            HttpServletRequest request) {
        return service.generate(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/nutrient-plans/{planId}/approve")
    NutrientDtos.PlanResponse approve(
            @PathVariable UUID planId,
            @Valid @RequestBody NutrientDtos.ApproveRequest body,
            HttpServletRequest request) {
        return service.approve(
                tenants.resolve(request).tenantId(), actorId(), planId, body);
    }

    @PostMapping("/nutrient-plans/{planId}/applications")
    Map<String, UUID> apply(
            @PathVariable UUID planId,
            @Valid @RequestBody NutrientDtos.ApplicationRequest body,
            HttpServletRequest request) {
        return Map.of("applicationId", service.recordApplication(
                tenants.resolve(request).tenantId(), actorId(), planId, body));
    }

    @GetMapping("/crop-cycles/{cycleId}/nutrient-plans")
    List<NutrientDtos.PlanResponse> plans(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.listPlans(tenants.resolve(request).tenantId(), cycleId);
    }

    @GetMapping("/crop-cycles/{cycleId}/nutrient-budget")
    List<NutrientDtos.BudgetResponse> budget(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.budget(tenants.resolve(request).tenantId(), cycleId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
