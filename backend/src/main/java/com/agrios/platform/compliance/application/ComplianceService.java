package com.agrios.platform.compliance.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.compliance.api.ComplianceDtos;
import com.agrios.platform.compliance.domain.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.farm.domain.FarmRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplianceService {
    private final StandardCatalogRepository standards;
    private final RequirementCatalogRepository requirements;
    private final ComplianceProfileRepository profiles;
    private final ComplianceObligationRepository obligations;
    private final CertificationApplicationRepository applications;
    private final InspectionRepository inspections;
    private final InspectionFindingRepository findings;
    private final SchemeCatalogRepository schemes;
    private final SchemeEligibilityAssessmentRepository schemeAssessments;
    private final FarmerRepository farmers;
    private final FarmRepository farms;
    private final ObjectMapper mapper;

    public ComplianceService(
            StandardCatalogRepository standards,
            RequirementCatalogRepository requirements,
            ComplianceProfileRepository profiles,
            ComplianceObligationRepository obligations,
            CertificationApplicationRepository applications,
            InspectionRepository inspections,
            InspectionFindingRepository findings,
            SchemeCatalogRepository schemes,
            SchemeEligibilityAssessmentRepository schemeAssessments,
            FarmerRepository farmers,
            FarmRepository farms,
            ObjectMapper mapper) {
        this.standards = standards;
        this.requirements = requirements;
        this.profiles = profiles;
        this.obligations = obligations;
        this.applications = applications;
        this.inspections = inspections;
        this.findings = findings;
        this.schemes = schemes;
        this.schemeAssessments = schemeAssessments;
        this.farmers = farmers;
        this.farms = farms;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createStandard(UUID tenantId, ComplianceDtos.StandardRequest request) {
        StandardCatalogEntity standard = standards.save(StandardCatalogEntity.create(
                tenantId, request.standardCode(), request.standardName(),
                request.standardType(), request.issuingAuthority(),
                request.jurisdictionCode(), request.versionLabel(),
                request.effectiveFrom(), request.effectiveUntil()));

        if (request.requirements() != null) {
            for (ComplianceDtos.RequirementRequest r : request.requirements()) {
                requirements.save(RequirementCatalogEntity.create(
                        standard.id(), r.requirementCode(), r.requirementTitle(),
                        r.requirementDescription(), r.requirementCategory(),
                        r.evidenceType(), r.mandatory(),
                        r.severityIfFailed(),
                        json(r.control() == null ? Map.of() : r.control())));
            }
        }
        return standard.id();
    }

    @Transactional
    public UUID createProfile(UUID tenantId, ComplianceDtos.ProfileRequest request) {
        if (request.farmerId() != null) {
            farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARMER_NOT_FOUND", "Farmer not found."));
        }
        if (request.farmId() != null) {
            farms.findByIdAndTenantId(request.farmId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARM_NOT_FOUND", "Farm not found."));
        }
        return profiles.save(ComplianceProfileEntity.create(
                tenantId, request.farmerId(), request.farmId(),
                request.profileName(), request.profileType(),
                request.geographyCode())).id();
    }

    @Transactional
    public UUID addObligation(UUID tenantId, UUID profileId,
                              ComplianceDtos.ObligationRequest request) {
        requireProfile(tenantId, profileId);
        standards.findByIdAndTenantId(request.standardId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "STANDARD_NOT_FOUND", "Standard not found."));
        return obligations.save(ComplianceObligationEntity.create(
                tenantId, profileId, request.standardId(),
                request.applicableFrom(), request.applicableUntil(),
                request.nextAssessmentDue())).id();
    }

    @Transactional
    public UUID createCertificationApplication(
            UUID tenantId, ComplianceDtos.CertificationApplicationRequest request) {
        requireProfile(tenantId, request.complianceProfileId());
        standards.findByIdAndTenantId(request.standardId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "STANDARD_NOT_FOUND", "Standard not found."));
        return applications.save(CertificationApplicationEntity.create(
                tenantId, request.complianceProfileId(),
                request.standardId(), request.applicationNumber(),
                request.applicationDate(), request.certificationBody(),
                json(request.requestedScope() == null
                        ? Map.of() : request.requestedScope()))).id();
    }

    @Transactional
    public void submitCertificationApplication(UUID tenantId, UUID applicationId) {
        applications.findByIdAndTenantId(applicationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CERTIFICATION_APPLICATION_NOT_FOUND",
                        "Certification application not found."))
                .submit();
    }

    @Transactional
    public UUID scheduleInspection(UUID tenantId,
                                   ComplianceDtos.InspectionRequest request) {
        requireProfile(tenantId, request.complianceProfileId());
        if (request.certificationApplicationId() != null) {
            applications.findByIdAndTenantId(
                    request.certificationApplicationId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "CERTIFICATION_APPLICATION_NOT_FOUND",
                            "Certification application not found."));
        }
        return inspections.save(InspectionEntity.create(
                tenantId, request.complianceProfileId(),
                request.certificationApplicationId(),
                request.inspectionType(), request.scheduledAt(),
                request.inspectorName(), request.inspectorReference(),
                request.notes())).id();
    }

    @Transactional
    public void startInspection(UUID tenantId, UUID inspectionId) {
        requireInspection(tenantId, inspectionId).start();
    }

    @Transactional
    public UUID addFinding(UUID tenantId, UUID inspectionId,
                           ComplianceDtos.FindingRequest request) {
        InspectionEntity inspection = requireInspection(tenantId, inspectionId);
        if (!"IN_PROGRESS".equals(inspection.status())) {
            throw new BusinessException("INSPECTION_NOT_IN_PROGRESS",
                    "Inspection must be in progress before adding findings.", 422);
        }
        return findings.save(InspectionFindingEntity.create(
                inspection.id(), request.requirementId(),
                request.findingCode(), request.findingType(),
                request.description(),
                json(request.evidence() == null ? Map.of() : request.evidence()),
                request.correctiveActionRequired(), request.dueDate())).id();
    }

    @Transactional
    public void completeInspection(
            UUID tenantId, UUID inspectionId,
            ComplianceDtos.InspectionCompleteRequest request) {
        requireInspection(tenantId, inspectionId)
                .complete(request.overallResult(), request.notes());
    }

    @Transactional
    public UUID createScheme(UUID tenantId, ComplianceDtos.SchemeRequest request) {
        return schemes.save(SchemeCatalogEntity.create(
                tenantId, request.schemeCode(), request.schemeName(),
                request.schemeType(), request.authorityName(),
                json(request.geographyCodes() == null
                        ? Set.of() : request.geographyCodes()),
                json(request.eligibilityRules() == null
                        ? Map.of() : request.eligibilityRules()),
                json(request.benefitDefinition() == null
                        ? Map.of() : request.benefitDefinition()),
                request.applicationUrl(), request.validFrom(),
                request.validUntil())).id();
    }

    @Transactional
    public ComplianceDtos.SchemeAssessmentResponse assessScheme(
            UUID tenantId, ComplianceDtos.SchemeAssessmentRequest request) {
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        SchemeCatalogEntity scheme = schemes.findByIdAndTenantId(
                        request.schemeId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SCHEME_NOT_FOUND", "Scheme not found."));

        Map<String,Object> evidence = request.evidenceSnapshot() == null
                ? Map.of() : request.evidenceSnapshot();
        JsonNode rules = readJson(scheme.eligibilityRules());

        List<String> reasons = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        int checks = 0;
        int passed = 0;

        if (rules.has("minimumLandAreaHectares")) {
            checks++;
            Object area = evidence.get("landAreaHectares");
            if (area == null) {
                missing.add("landAreaHectares");
            } else if (new BigDecimal(area.toString()).compareTo(
                    rules.get("minimumLandAreaHectares").decimalValue()) >= 0) {
                passed++;
                reasons.add("MINIMUM_LAND_AREA_MET");
            } else {
                reasons.add("MINIMUM_LAND_AREA_NOT_MET");
            }
        }

        if (rules.has("requiredStateCode")) {
            checks++;
            Object state = evidence.get("stateCode");
            if (state == null) {
                missing.add("stateCode");
            } else if (rules.get("requiredStateCode").asText()
                    .equalsIgnoreCase(state.toString())) {
                passed++;
                reasons.add("STATE_REQUIREMENT_MET");
            } else {
                reasons.add("STATE_REQUIREMENT_NOT_MET");
            }
        }

        String status;
        BigDecimal score;
        if (!missing.isEmpty()) {
            status = "INCOMPLETE_DATA";
            score = checks == 0 ? BigDecimal.ZERO
                    : BigDecimal.valueOf(passed)
                    .divide(BigDecimal.valueOf(checks), 4, java.math.RoundingMode.HALF_UP);
        } else if (checks == 0 || passed == checks) {
            status = "ELIGIBLE";
            score = BigDecimal.ONE;
        } else if (passed > 0) {
            status = "POSSIBLY_ELIGIBLE";
            score = BigDecimal.valueOf(passed)
                    .divide(BigDecimal.valueOf(checks), 4, java.math.RoundingMode.HALF_UP);
        } else {
            status = "NOT_ELIGIBLE";
            score = BigDecimal.ZERO;
        }

        SchemeEligibilityAssessmentEntity assessment =
                SchemeEligibilityAssessmentEntity.create(
                        tenantId, request.farmerId(), request.schemeId(),
                        status, score, json(reasons), json(missing),
                        json(evidence));
        return ComplianceDtos.SchemeAssessmentResponse.from(
                schemeAssessments.save(assessment));
    }

    private ComplianceProfileEntity requireProfile(UUID tenantId, UUID profileId) {
        return profiles.findByIdAndTenantId(profileId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "COMPLIANCE_PROFILE_NOT_FOUND",
                        "Compliance profile not found."));
    }

    private InspectionEntity requireInspection(UUID tenantId, UUID inspectionId) {
        return inspections.findByIdAndTenantId(inspectionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "INSPECTION_NOT_FOUND", "Inspection not found."));
    }

    private JsonNode readJson(String value) {
        try {
            return mapper.readTree(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(
                    "JSON_DESERIALIZATION_FAILED",
                    "Unable to read compliance rule data.", 500);
        }
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(
                    "JSON_SERIALIZATION_FAILED",
                    "Unable to serialize compliance data.", 500);
        }
    }
}
