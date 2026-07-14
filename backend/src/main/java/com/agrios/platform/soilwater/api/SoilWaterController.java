package com.agrios.platform.soilwater.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.soilwater.application.SoilWaterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SoilWaterController {
    private final SoilWaterService service;
    private final TenantContextResolver tenants;

    public SoilWaterController(SoilWaterService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/laboratories")
    SoilWaterDtos.LaboratoryResponse createLaboratory(
            @Valid @RequestBody SoilWaterDtos.LaboratoryRequest body,
            HttpServletRequest request) {
        return service.createLaboratory(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @GetMapping("/laboratories")
    List<SoilWaterDtos.LaboratoryResponse> listLaboratories(HttpServletRequest request) {
        return service.listLaboratories(tenants.resolve(request).tenantId());
    }

    @PostMapping("/soil-samples")
    SoilWaterDtos.SampleResponse collectSoilSample(
            @Valid @RequestBody SoilWaterDtos.SoilSampleRequest body,
            HttpServletRequest request) {
        return service.collectSoilSample(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/water-samples")
    SoilWaterDtos.SampleResponse collectWaterSample(
            @Valid @RequestBody SoilWaterDtos.WaterSampleRequest body,
            HttpServletRequest request) {
        return service.collectWaterSample(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/test-reports")
    SoilWaterDtos.TestReportResponse createReport(
            @Valid @RequestBody SoilWaterDtos.TestReportRequest body,
            HttpServletRequest request) {
        return service.createReport(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/test-reports/{reportId}/publish")
    SoilWaterDtos.ProfileResponse publishReport(
            @PathVariable UUID reportId, HttpServletRequest request) {
        return service.publishReport(tenants.resolve(request).tenantId(), actorId(), reportId);
    }

    @GetMapping("/fields/{fieldId}/soil-profile")
    SoilWaterDtos.ProfileResponse soilProfile(
            @PathVariable UUID fieldId, HttpServletRequest request) {
        return service.currentSoilProfile(tenants.resolve(request).tenantId(), fieldId);
    }

    @GetMapping("/water-sources/{waterSourceId}/quality-profile")
    SoilWaterDtos.ProfileResponse waterProfile(
            @PathVariable UUID waterSourceId, HttpServletRequest request) {
        return service.currentWaterProfile(tenants.resolve(request).tenantId(), waterSourceId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
