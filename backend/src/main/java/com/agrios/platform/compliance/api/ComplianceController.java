package com.agrios.platform.compliance.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.compliance.application.ComplianceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ComplianceController {
    private final ComplianceService service;
    private final TenantContextResolver tenants;

    public ComplianceController(
            ComplianceService service,
            TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/compliance/standards")
    Map<String, UUID> standard(
            @Valid @RequestBody ComplianceDtos.StandardRequest body,
            HttpServletRequest request) {
        return Map.of("standardId", service.createStandard(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/compliance/profiles")
    Map<String, UUID> profile(
            @Valid @RequestBody ComplianceDtos.ProfileRequest body,
            HttpServletRequest request) {
        return Map.of("profileId", service.createProfile(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/compliance/profiles/{profileId}/obligations")
    Map<String, UUID> obligation(
            @PathVariable UUID profileId,
            @Valid @RequestBody ComplianceDtos.ObligationRequest body,
            HttpServletRequest request) {
        return Map.of("obligationId", service.addObligation(
                tenants.resolve(request).tenantId(), profileId, body));
    }

    @PostMapping("/compliance/certification-applications")
    Map<String, UUID> certificationApplication(
            @Valid @RequestBody ComplianceDtos.CertificationApplicationRequest body,
            HttpServletRequest request) {
        return Map.of("certificationApplicationId",
                service.createCertificationApplication(
                        tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/compliance/certification-applications/{applicationId}/submit")
    void submitApplication(
            @PathVariable UUID applicationId,
            HttpServletRequest request) {
        service.submitCertificationApplication(
                tenants.resolve(request).tenantId(), applicationId);
    }

    @PostMapping("/compliance/inspections")
    Map<String, UUID> inspection(
            @Valid @RequestBody ComplianceDtos.InspectionRequest body,
            HttpServletRequest request) {
        return Map.of("inspectionId", service.scheduleInspection(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/compliance/inspections/{inspectionId}/start")
    void startInspection(
            @PathVariable UUID inspectionId,
            HttpServletRequest request) {
        service.startInspection(
                tenants.resolve(request).tenantId(), inspectionId);
    }

    @PostMapping("/compliance/inspections/{inspectionId}/findings")
    Map<String, UUID> finding(
            @PathVariable UUID inspectionId,
            @Valid @RequestBody ComplianceDtos.FindingRequest body,
            HttpServletRequest request) {
        return Map.of("findingId", service.addFinding(
                tenants.resolve(request).tenantId(), inspectionId, body));
    }

    @PostMapping("/compliance/inspections/{inspectionId}/complete")
    void completeInspection(
            @PathVariable UUID inspectionId,
            @Valid @RequestBody ComplianceDtos.InspectionCompleteRequest body,
            HttpServletRequest request) {
        service.completeInspection(
                tenants.resolve(request).tenantId(), inspectionId, body);
    }

    @PostMapping("/compliance/schemes")
    Map<String, UUID> scheme(
            @Valid @RequestBody ComplianceDtos.SchemeRequest body,
            HttpServletRequest request) {
        return Map.of("schemeId", service.createScheme(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/compliance/schemes/eligibility-assessments")
    ComplianceDtos.SchemeAssessmentResponse schemeAssessment(
            @Valid @RequestBody ComplianceDtos.SchemeAssessmentRequest body,
            HttpServletRequest request) {
        return service.assessScheme(
                tenants.resolve(request).tenantId(), body);
    }
}
