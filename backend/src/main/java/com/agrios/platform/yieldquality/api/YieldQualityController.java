package com.agrios.platform.yieldquality.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.yieldquality.application.YieldQualityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class YieldQualityController {
    private final YieldQualityService service;
    private final TenantContextResolver tenants;

    public YieldQualityController(YieldQualityService service,
                                  TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/yield-records")
    Map<String, UUID> yield(
            @Valid @RequestBody YieldQualityDtos.YieldRequest body,
            HttpServletRequest request) {
        return Map.of("yieldRecordId", service.recordYield(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/quality/parameters")
    Map<String, UUID> parameter(
            @Valid @RequestBody YieldQualityDtos.ParameterRequest body,
            HttpServletRequest request) {
        return Map.of("qualityParameterId", service.createParameter(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/quality/assessments")
    Map<String, UUID> assessment(
            @Valid @RequestBody YieldQualityDtos.AssessmentRequest body,
            HttpServletRequest request) {
        return Map.of("qualityAssessmentId", service.createAssessment(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/quality/assessments/{assessmentId}/complete")
    void complete(
            @PathVariable UUID assessmentId,
            @Valid @RequestBody YieldQualityDtos.CompleteAssessmentRequest body,
            HttpServletRequest request) {
        service.completeAssessment(
                tenants.resolve(request).tenantId(), assessmentId, body);
    }
}
