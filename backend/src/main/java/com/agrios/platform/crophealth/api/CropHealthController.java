package com.agrios.platform.crophealth.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.crophealth.application.CropHealthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CropHealthController {
    private final CropHealthService service;
    private final TenantContextResolver tenants;

    public CropHealthController(CropHealthService service,
                                TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/crop-health/pests")
    Map<String, UUID> createPest(
            @Valid @RequestBody CropHealthDtos.CatalogRequest body,
            HttpServletRequest request) {
        return Map.of("pestId", service.createPest(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/crop-health/pests/{pestId}/publish")
    void publishPest(@PathVariable UUID pestId, HttpServletRequest request) {
        service.publishPest(tenants.resolve(request).tenantId(), pestId);
    }

    @PostMapping("/crop-health/diseases")
    Map<String, UUID> createDisease(
            @Valid @RequestBody CropHealthDtos.CatalogRequest body,
            HttpServletRequest request) {
        return Map.of("diseaseId", service.createDisease(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/crop-health/diseases/{diseaseId}/publish")
    void publishDisease(@PathVariable UUID diseaseId, HttpServletRequest request) {
        service.publishDisease(tenants.resolve(request).tenantId(), diseaseId);
    }

    @PostMapping("/crop-health/scouting-visits")
    Map<String, UUID> scouting(
            @Valid @RequestBody CropHealthDtos.ScoutingRequest body,
            HttpServletRequest request) {
        return Map.of("scoutingVisitId", service.scout(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/crop-health/assessments")
    CropHealthDtos.HealthAssessmentResponse assess(
            @Valid @RequestBody CropHealthDtos.HealthAssessmentRequest body,
            HttpServletRequest request) {
        return service.assess(tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/crop-health/outbreaks")
    Map<String, UUID> outbreak(
            @Valid @RequestBody CropHealthDtos.OutbreakRequest body,
            HttpServletRequest request) {
        return Map.of("outbreakId", service.openOutbreak(
                tenants.resolve(request).tenantId(), body));
    }
}
