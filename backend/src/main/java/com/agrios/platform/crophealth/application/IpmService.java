package com.agrios.platform.crophealth.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.crophealth.api.IpmDtos;
import com.agrios.platform.crophealth.domain.*;
import com.agrios.platform.farm.domain.FieldRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IpmService {
    private final IpmPlanRepository plans;
    private final IpmActionRepository actions;
    private final IpmExecutionRepository executions;
    private final PestCatalogRepository pests;
    private final DiseaseCatalogRepository diseases;
    private final CropCycleRepository cycles;
    private final FieldRepository fields;
    private final ObjectMapper mapper;

    public IpmService(IpmPlanRepository plans,
                      IpmActionRepository actions,
                      IpmExecutionRepository executions,
                      PestCatalogRepository pests,
                      DiseaseCatalogRepository diseases,
                      CropCycleRepository cycles,
                      FieldRepository fields,
                      ObjectMapper mapper) {
        this.plans = plans;
        this.actions = actions;
        this.executions = executions;
        this.pests = pests;
        this.diseases = diseases;
        this.cycles = cycles;
        this.fields = fields;
        this.mapper = mapper;
    }

    @Transactional
    public IpmDtos.PlanResponse generate(UUID tenantId, IpmDtos.GenerateRequest request) {
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        if (!cycle.fieldId().equals(request.fieldId())) {
            throw new ConflictException("IPM_FIELD_CYCLE_MISMATCH",
                    "Crop cycle does not belong to field.");
        }

        validateSubject(tenantId, request.planType(), request.pestId(), request.diseaseId());

        IpmPlanEntity plan = plans.save(IpmPlanEntity.create(
                tenantId, cycle.id(), request.fieldId(),
                request.pestId(), request.diseaseId(),
                request.planType(), request.strategySummary(),
                json(request.preventionMeasures() == null ? List.of() : request.preventionMeasures()),
                json(request.monitoringMeasures() == null ? List.of() : request.monitoringMeasures()),
                json(request.nonChemicalMeasures() == null ? List.of() : request.nonChemicalMeasures()),
                request.chemicalLastResort(),
                json(request.warnings() == null ? List.of() : request.warnings())));

        if (request.actions() != null) {
            for (IpmDtos.ActionRequest a : request.actions()) {
                if ("CHEMICAL".equals(a.actionCategory()) && !request.chemicalLastResort()) {
                    throw new BusinessException("CHEMICAL_ACTION_NOT_ALLOWED",
                            "Chemical action is not allowed for this IPM plan.", 422);
                }
                actions.save(IpmActionEntity.create(
                        plan.id(), a.actionSequence(), a.actionCategory(),
                        a.actionName(), a.actionDescription(),
                        a.triggerCondition(), a.cropStageCode(),
                        a.plannedDate(), a.productName(), a.dosage(),
                        a.dosageUnit(), a.applicationMethod(),
                        a.preHarvestIntervalDays(), a.reentryIntervalHours(),
                        a.pollinatorWarning(), a.waterBodyBufferMeters()));
            }
        }

        return IpmDtos.PlanResponse.from(plan);
    }

    @Transactional
    public IpmDtos.PlanResponse approve(UUID tenantId, UUID actorId,
                                        UUID planId, IpmDtos.ApproveRequest request) {
        IpmPlanEntity plan = requirePlan(tenantId, planId);
        if (actions.findByIpmPlanIdOrderByActionSequence(plan.id()).isEmpty()) {
            throw new BusinessException("IPM_PLAN_EMPTY",
                    "At least one IPM action is required.", 422);
        }
        plan.approve(actorId, request.approvalNotes());
        return IpmDtos.PlanResponse.from(plan);
    }

    @Transactional
    public UUID execute(UUID tenantId, UUID actorId,
                        UUID planId, IpmDtos.ExecuteRequest request) {
        IpmPlanEntity plan = requirePlan(tenantId, planId);
        if (!plan.status().equals("APPROVED") && !plan.status().equals("ACTIVE")) {
            throw new BusinessException("IPM_PLAN_NOT_APPROVED",
                    "IPM execution requires an approved plan.", 422);
        }
        IpmActionEntity action = actions.findById(request.ipmActionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "IPM_ACTION_NOT_FOUND", "IPM action not found."));

        String safety = "UNSAFE".equals(request.weatherCheckStatus())
                ? "BLOCKED" : "VALID";
        if ("BLOCKED".equals(safety)) {
            throw new BusinessException("IPM_EXECUTION_WEATHER_UNSAFE",
                    "IPM execution is blocked by weather safety validation.", 422);
        }

        IpmExecutionEntity value = executions.save(IpmExecutionEntity.create(
                plan.id(), action.id(), plan.cropCycleId(),
                request.executedAt(), request.executedQuantity(),
                request.quantityUnit(), request.coveredAreaHectares(),
                request.weatherCheckStatus(), safety,
                request.observedResult(), request.notes(), actorId));
        return value.id();
    }

    @Transactional(readOnly = true)
    public List<IpmDtos.PlanResponse> list(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return plans.findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(tenantId, cycleId)
                .stream().map(IpmDtos.PlanResponse::from).toList();
    }

    private void validateSubject(UUID tenantId, String type, UUID pestId, UUID diseaseId) {
        if ("PEST".equals(type)) {
            pests.findByIdAndTenantId(pestId, tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "PEST_NOT_FOUND", "Pest not found."));
        } else if ("DISEASE".equals(type)) {
            diseases.findByIdAndTenantId(diseaseId, tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "DISEASE_NOT_FOUND", "Disease not found."));
        } else if (!"GENERAL_IPM".equals(type)) {
            throw new BusinessException("IPM_PLAN_TYPE_INVALID",
                    "IPM plan type is invalid.", 422);
        }
    }

    private IpmPlanEntity requirePlan(UUID tenantId, UUID planId) {
        return plans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "IPM_PLAN_NOT_FOUND", "IPM plan not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize IPM data.", 500);
        }
    }
}
