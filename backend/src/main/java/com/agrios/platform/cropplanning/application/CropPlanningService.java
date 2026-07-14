package com.agrios.platform.cropplanning.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropplanning.api.CropPlanningDtos;
import com.agrios.platform.cropplanning.domain.*;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.knowledge.domain.*;
import com.agrios.platform.soilwater.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.util.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropPlanningService {
    private final AssessmentRepository assessments;
    private final CropPlanRepository plans;
    private final FieldRepository fields;
    private final CropRepository crops;
    private final VarietyRepository varieties;
    private final CropRequirementRepository requirements;
    private final ProfileRepository profiles;
    private final ObjectMapper mapper;

    public CropPlanningService(AssessmentRepository assessments,
                               CropPlanRepository plans,
                               FieldRepository fields,
                               CropRepository crops,
                               VarietyRepository varieties,
                               CropRequirementRepository requirements,
                               ProfileRepository profiles,
                               ObjectMapper mapper) {
        this.assessments = assessments;
        this.plans = plans;
        this.fields = fields;
        this.crops = crops;
        this.varieties = varieties;
        this.requirements = requirements;
        this.profiles = profiles;
        this.mapper = mapper;
    }

    @Transactional
    public CropPlanningDtos.AssessmentResponse assess(
            UUID tenantId, UUID actorId, CropPlanningDtos.AssessmentRequest request) {
        FieldEntity field = fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));

        Optional<ProfileEntity> soil = profiles
                .findByTenantIdAndFieldIdAndProfileTypeAndIsCurrentTrue(
                        tenantId, field.id(), "SOIL");

        Set<UUID> selectedIds = request.cropIds() == null ? Set.of() : request.cropIds();
        List<CropEntity> candidates = crops.findByTenantIdAndStatus(
                        tenantId, KnowledgeStatus.PUBLISHED, Pageable.unpaged())
                .stream().filter(c -> selectedIds.isEmpty() || selectedIds.contains(c.id()))
                .toList();

        if (candidates.isEmpty()) {
            throw new BusinessException("NO_PUBLISHED_CROPS",
                    "No published crops are available for assessment.", 422);
        }

        SuitabilityAssessmentEntity assessment = SuitabilityAssessmentEntity.create(
                tenantId, field.id(), actorId, request.seasonCode(),
                request.farmingSystem(), soil.map(ProfileEntity::id).orElse(null),
                null, json(request.assumptions() == null ? Map.of() : request.assumptions()));

        Map<String, BigDecimal> soilValues = soil
                .map(p -> numericProfileValues(p.summaryJson()))
                .orElseGet(Map::of);

        record Scored(CropEntity crop, BigDecimal score, boolean failed,
                      List<String> hard, List<String> reasons) {}
        List<Scored> scored = new ArrayList<>();
        for (CropEntity crop : candidates) {
            BigDecimal score = new BigDecimal("100.0000");
            boolean hardFailed = false;
            List<String> hardCodes = new ArrayList<>();
            List<String> reasons = new ArrayList<>();

            for (CropRequirementEntity req : requirements.findByCropId(crop.id())) {
                BigDecimal observed = soilValues.get(req.requirementType());
                if (observed == null) {
                    score = score.subtract(new BigDecimal("5.0000"));
                    reasons.add(req.requirementType() + "_DATA_MISSING");
                    continue;
                }
                boolean below = req.minimumValue() != null &&
                        observed.compareTo(req.minimumValue()) < 0;
                boolean above = req.maximumValue() != null &&
                        observed.compareTo(req.maximumValue()) > 0;
                if (below || above) {
                    score = score.subtract(req.hardConstraint()
                            ? new BigDecimal("60.0000") : new BigDecimal("15.0000"));
                    reasons.add(req.requirementType() + "_OUTSIDE_RANGE");
                    if (req.hardConstraint()) {
                        hardFailed = true;
                        hardCodes.add(req.requirementType() + "_HARD_CONSTRAINT");
                    }
                } else {
                    reasons.add(req.requirementType() + "_SUITABLE");
                }
            }

            if (score.signum() < 0) score = BigDecimal.ZERO;
            scored.add(new Scored(crop, score, hardFailed, hardCodes, reasons));
        }

        scored.sort(Comparator
                .comparing(Scored::failed)
                .thenComparing(Scored::score, Comparator.reverseOrder()));

        int rank = 1;
        for (Scored value : scored) {
            BigDecimal confidence = soil.isPresent()
                    ? new BigDecimal("0.8000") : new BigDecimal("0.4500");
            assessment.addCandidate(value.crop().id(), null,
                    value.score(), confidence, value.failed(),
                    json(value.hard()), json(value.reasons()),
                    rank++, value.crop().durationMaxDays());
        }

        return CropPlanningDtos.AssessmentResponse.from(assessments.save(assessment));
    }

    @Transactional(readOnly = true)
    public CropPlanningDtos.AssessmentResponse getAssessment(
            UUID tenantId, UUID assessmentId) {
        return CropPlanningDtos.AssessmentResponse.from(
                assessments.findByIdAndTenantId(assessmentId, tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "ASSESSMENT_NOT_FOUND", "Suitability assessment not found.")));
    }

    @Transactional
    public CropPlanningDtos.PlanResponse createPlan(
            UUID tenantId, UUID actorId, CropPlanningDtos.CreatePlanRequest request) {
        FieldEntity field = fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        SuitabilityAssessmentEntity assessment = assessments
                .findByIdAndTenantId(request.assessmentId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ASSESSMENT_NOT_FOUND", "Suitability assessment not found."));
        if (!assessment.fieldId().equals(field.id())) {
            throw new ConflictException("ASSESSMENT_FIELD_MISMATCH",
                    "Assessment belongs to another field.");
        }
        CropEntity crop = crops.findByIdAndTenantId(request.selectedCropId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_NOT_FOUND", "Selected crop not found."));
        if (crop.status() != KnowledgeStatus.PUBLISHED) {
            throw new BusinessException("CROP_NOT_PUBLISHED",
                    "Selected crop is not published.", 422);
        }
        if (request.plannedAreaHectares().compareTo(field.areaHectares()) > 0) {
            throw new BusinessException("CROP_PLAN_AREA_EXCEEDS_FIELD",
                    "Planned area exceeds field area.", 422);
        }
        if (request.selectedVarietyId() != null) {
            VarietyEntity variety = varieties
                    .findByIdAndTenantId(request.selectedVarietyId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "VARIETY_NOT_FOUND", "Selected variety not found."));
            if (!variety.cropId().equals(crop.id())) {
                throw new ConflictException("VARIETY_CROP_MISMATCH",
                        "Selected variety does not belong to selected crop.");
            }
        }

        CropPlanEntity plan = CropPlanEntity.create(
                tenantId, field.id(), assessment.id(),
                request.seasonCode(), request.farmingSystem(),
                crop.id(), request.selectedVarietyId(),
                request.plannedAreaHectares(), actorId);
        return CropPlanningDtos.PlanResponse.from(plans.save(plan));
    }

    @Transactional
    public CropPlanningDtos.PlanResponse approvePlan(
            UUID tenantId, UUID actorId, UUID planId,
            CropPlanningDtos.ApprovePlanRequest request) {
        CropPlanEntity plan = plans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_PLAN_NOT_FOUND", "Crop plan not found."));
        SuitabilityAssessmentEntity assessment = assessments
                .findByIdAndTenantId(plan.assessmentId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ASSESSMENT_NOT_FOUND", "Suitability assessment not found."));
        boolean hardFailed = assessment.candidates().stream()
                .filter(c -> c.cropId().equals(plan.selectedCropId()))
                .anyMatch(SuitabilityCandidateEntity::hardConstraintFailed);
        if (hardFailed) {
            throw new BusinessException("CROP_HARD_CONSTRAINT_FAILED",
                    "Selected crop has unresolved hard constraints.", 422);
        }
        plan.approve(request.approvalNotes(), actorId);
        return CropPlanningDtos.PlanResponse.from(plan);
    }

    @Transactional(readOnly = true)
    public List<CropPlanningDtos.PlanResponse> listPlans(UUID tenantId, UUID fieldId) {
        fields.findByIdAndTenantId(fieldId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        return plans.findByTenantIdAndFieldIdOrderByCreatedAtDesc(tenantId, fieldId)
                .stream().map(CropPlanningDtos.PlanResponse::from).toList();
    }

    private Map<String, BigDecimal> numericProfileValues(String json) {
        try {
            Map<String, Map<String, Object>> raw = mapper.readValue(
                    json, new TypeReference<>() {});
            Map<String, BigDecimal> result = new HashMap<>();
            raw.forEach((code, value) -> {
                Object number = value.get("value");
                if (number != null) result.put(code, new BigDecimal(number.toString()));
            });
            return result;
        } catch (Exception ex) {
            return Map.of();
        }
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize planning data.", 500);
        }
    }
}
