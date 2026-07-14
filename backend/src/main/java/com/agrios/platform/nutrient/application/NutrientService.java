package com.agrios.platform.nutrient.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.nutrient.api.NutrientDtos;
import com.agrios.platform.nutrient.domain.*;
import com.agrios.platform.soilwater.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NutrientService {
    private final NutrientPlanRepository plans;
    private final NutrientPlanItemRepository items;
    private final NutrientApplicationRepository applications;
    private final NutrientBudgetRepository budgets;
    private final CropCycleRepository cycles;
    private final ProfileRepository profiles;
    private final ObjectMapper mapper;

    public NutrientService(NutrientPlanRepository plans,
                           NutrientPlanItemRepository items,
                           NutrientApplicationRepository applications,
                           NutrientBudgetRepository budgets,
                           CropCycleRepository cycles,
                           ProfileRepository profiles,
                           ObjectMapper mapper) {
        this.plans = plans;
        this.items = items;
        this.applications = applications;
        this.budgets = budgets;
        this.cycles = cycles;
        this.profiles = profiles;
        this.mapper = mapper;
    }

    @Transactional
    public NutrientDtos.PlanResponse generate(
            UUID tenantId, UUID actorId, NutrientDtos.GeneratePlanRequest request) {
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        ensureWritable(cycle);

        Optional<ProfileEntity> soil = profiles
                .findByTenantIdAndFieldIdAndProfileTypeAndIsCurrentTrue(
                        tenantId, cycle.fieldId(), "SOIL");

        List<String> warnings = new ArrayList<>();
        if (soil.isEmpty()) warnings.add("SOIL_PROFILE_MISSING");

        if (request.farmingApproach() == FarmingApproach.FERTILIZER_FREE &&
                request.items() != null &&
                request.items().stream().anyMatch(i ->
                        "SYNTHETIC_FERTILIZER".equals(i.sourceCategory()))) {
            throw new BusinessException("FERTILIZER_FREE_SOURCE_NOT_ALLOWED",
                    "Synthetic fertilizer cannot be used in a fertilizer-free plan.", 422);
        }

        Map<String,Object> basis = new LinkedHashMap<>();
        basis.put("cropCycleId", cycle.id());
        basis.put("fieldId", cycle.fieldId());
        basis.put("soilProfileId", soil.map(ProfileEntity::id).orElse(null));
        basis.put("approach", request.farmingApproach().name());
        basis.put("calculationVersion", "v1");

        NutrientPlanEntity plan = plans.save(NutrientPlanEntity.create(
                tenantId, cycle.id(), soil.map(ProfileEntity::id).orElse(null),
                request.farmingApproach(), request.targetYield(),
                request.targetYieldUnit(), json(basis), json(warnings), actorId));

        if (request.items() != null) {
            request.items().forEach(i -> items.save(NutrientPlanItemEntity.create(
                    plan.id(), i.nutrientCode(), i.sourceCategory(),
                    i.sourceName(), i.plannedQuantity(), i.quantityUnit(),
                    i.nutrientContentPercent(), i.plannedNutrientQuantity(),
                    i.plannedApplicationDate(), i.cropStageCode(),
                    i.applicationMethod(), i.splitNo(),
                    i.safetyIntervalDays(), i.notes())));
        }

        recalculateBudget(cycle.id(), plan.id());
        return NutrientDtos.PlanResponse.from(plan);
    }

    @Transactional
    public NutrientDtos.PlanResponse approve(UUID tenantId, UUID actorId,
                                              UUID planId,
                                              NutrientDtos.ApproveRequest request) {
        NutrientPlanEntity plan = requirePlan(tenantId, planId);
        if (items.findByNutrientPlanId(plan.id()).isEmpty()) {
            throw new BusinessException("NUTRIENT_PLAN_EMPTY",
                    "At least one nutrient-plan item is required.", 422);
        }
        plan.approve(request.approvalNotes(), actorId);
        return NutrientDtos.PlanResponse.from(plan);
    }

    @Transactional
    public UUID recordApplication(UUID tenantId, UUID actorId, UUID planId,
                                  NutrientDtos.ApplicationRequest request) {
        NutrientPlanEntity plan = requirePlan(tenantId, planId);
        if (!plan.status().equals("APPROVED") && !plan.status().equals("ACTIVE")) {
            throw new BusinessException("NUTRIENT_PLAN_NOT_APPROVED",
                    "Nutrient applications require an approved plan.", 422);
        }
        CropCycleEntity cycle = cycles.findByIdAndTenantId(plan.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        ensureWritable(cycle);
        if (request.areaHectares() != null &&
                request.areaHectares().compareTo(cycle.plannedAreaHectares()) > 0) {
            throw new BusinessException("APPLICATION_AREA_EXCEEDS_CROP_AREA",
                    "Application area exceeds crop-cycle area.", 422);
        }
        String safety = request.weatherCheckStatus().equals("UNSAFE")
                ? "BLOCKED" : "VALID";
        if (safety.equals("BLOCKED")) {
            throw new BusinessException("NUTRIENT_APPLICATION_WEATHER_UNSAFE",
                    "Application is blocked by weather safety validation.", 422);
        }

        NutrientApplicationEntity application = applications.save(
                NutrientApplicationEntity.create(
                        plan.id(), request.nutrientPlanItemId(), cycle.id(),
                        request.appliedAt(), request.sourceCategory(),
                        request.sourceName(), request.appliedQuantity(),
                        request.quantityUnit(), request.applicationMethod(),
                        request.areaHectares(), request.weatherCheckStatus(),
                        safety, request.notes(), actorId));
        recalculateBudget(cycle.id(), plan.id());
        return application.id();
    }

    @Transactional(readOnly = true)
    public List<NutrientDtos.PlanResponse> listPlans(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return plans.findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(tenantId, cycleId)
                .stream().map(NutrientDtos.PlanResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<NutrientDtos.BudgetResponse> budget(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return budgets.findByCropCycleId(cycleId).stream()
                .map(NutrientDtos.BudgetResponse::from).toList();
    }

    private void recalculateBudget(UUID cycleId, UUID planId) {
        budgets.deleteByCropCycleId(cycleId);
        Map<String, BigDecimal> planned = new LinkedHashMap<>();
        for (NutrientPlanItemEntity item : items.findByNutrientPlanId(planId)) {
            BigDecimal quantity = item.plannedNutrientQuantity() == null
                    ? item.plannedQuantity() : item.plannedNutrientQuantity();
            planned.merge(item.nutrientCode(), quantity, BigDecimal::add);
        }
        BigDecimal totalApplied = applications.findByCropCycleId(cycleId).stream()
                .map(NutrientApplicationEntity::appliedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        for (Map.Entry<String, BigDecimal> entry : planned.entrySet()) {
            budgets.save(NutrientBudgetEntity.create(
                    cycleId, entry.getKey(), entry.getValue(),
                    totalApplied, null, "KG"));
        }
    }

    private NutrientPlanEntity requirePlan(UUID tenantId, UUID planId) {
        return plans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "NUTRIENT_PLAN_NOT_FOUND", "Nutrient plan not found."));
    }

    private void ensureWritable(CropCycleEntity cycle) {
        if (cycle.status() == CropCycleStatus.CLOSED ||
                cycle.status() == CropCycleStatus.CANCELLED) {
            throw new BusinessException("CROP_CYCLE_READ_ONLY",
                    "Closed crop cycle is read-only.", 422);
        }
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize nutrient-plan data.", 500);
        }
    }
}
