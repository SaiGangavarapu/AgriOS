package com.agrios.platform.cropplanning.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.cropplanning.application.CropPlanningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CropPlanningController {
    private final CropPlanningService service;
    private final TenantContextResolver tenants;

    public CropPlanningController(CropPlanningService service,
                                  TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/crop-suitability-assessments")
    CropPlanningDtos.AssessmentResponse assess(
            @Valid @RequestBody CropPlanningDtos.AssessmentRequest body,
            HttpServletRequest request) {
        return service.assess(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @GetMapping("/crop-suitability-assessments/{assessmentId}")
    CropPlanningDtos.AssessmentResponse getAssessment(
            @PathVariable UUID assessmentId, HttpServletRequest request) {
        return service.getAssessment(tenants.resolve(request).tenantId(), assessmentId);
    }

    @PostMapping("/crop-plans")
    CropPlanningDtos.PlanResponse createPlan(
            @Valid @RequestBody CropPlanningDtos.CreatePlanRequest body,
            HttpServletRequest request) {
        return service.createPlan(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/crop-plans/{planId}/approve")
    CropPlanningDtos.PlanResponse approvePlan(
            @PathVariable UUID planId,
            @Valid @RequestBody CropPlanningDtos.ApprovePlanRequest body,
            HttpServletRequest request) {
        return service.approvePlan(tenants.resolve(request).tenantId(),
                actorId(), planId, body);
    }

    @GetMapping("/fields/{fieldId}/crop-plans")
    List<CropPlanningDtos.PlanResponse> listPlans(
            @PathVariable UUID fieldId, HttpServletRequest request) {
        return service.listPlans(tenants.resolve(request).tenantId(), fieldId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
