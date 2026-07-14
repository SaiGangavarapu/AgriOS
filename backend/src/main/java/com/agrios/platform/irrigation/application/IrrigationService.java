package com.agrios.platform.irrigation.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.irrigation.api.IrrigationDtos;
import com.agrios.platform.irrigation.domain.*;
import com.agrios.platform.soilwater.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IrrigationService {
    private final IrrigationPlanRepository plans;
    private final IrrigationScheduleRepository schedules;
    private final IrrigationExecutionRepository executions;
    private final WaterAccountingRepository accounting;
    private final CropCycleRepository cycles;
    private final WaterSourceRepository waterSources;
    private final ProfileRepository profiles;
    private final ObjectMapper mapper;

    public IrrigationService(IrrigationPlanRepository plans,
                             IrrigationScheduleRepository schedules,
                             IrrigationExecutionRepository executions,
                             WaterAccountingRepository accounting,
                             CropCycleRepository cycles,
                             WaterSourceRepository waterSources,
                             ProfileRepository profiles,
                             ObjectMapper mapper) {
        this.plans = plans;
        this.schedules = schedules;
        this.executions = executions;
        this.accounting = accounting;
        this.cycles = cycles;
        this.waterSources = waterSources;
        this.profiles = profiles;
        this.mapper = mapper;
    }

    @Transactional
    public IrrigationDtos.PlanResponse generate(
            UUID tenantId, UUID actorId, IrrigationDtos.GeneratePlanRequest request) {
        CropCycleEntity cycle = requireCycle(tenantId, request.cropCycleId());
        ensureWritable(cycle);

        UUID profileId = null;
        List<String> warnings = new ArrayList<>();
        if (request.waterSourceId() != null) {
            waterSources.findByIdAndTenantId(request.waterSourceId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "WATER_SOURCE_NOT_FOUND", "Water source not found."));
            profileId = profiles
                    .findByTenantIdAndWaterSourceIdAndProfileTypeAndIsCurrentTrue(
                            tenantId, request.waterSourceId(), "WATER")
                    .map(ProfileEntity::id).orElse(null);
            if (profileId == null) warnings.add("WATER_PROFILE_MISSING");
        } else {
            warnings.add("WATER_SOURCE_NOT_SELECTED");
        }

        Map<String,Object> basis = new LinkedHashMap<>();
        basis.put("cropCycleId", cycle.id());
        basis.put("fieldId", cycle.fieldId());
        basis.put("areaHectares", cycle.plannedAreaHectares());
        basis.put("calculationVersion", "v1");

        IrrigationPlanEntity plan = IrrigationPlanEntity.create(
                tenantId, cycle.id(), request.waterSourceId(), profileId,
                request.irrigationMethod(),
                request.seasonalWaterRequirementMm(),
                request.plannedWaterVolumeM3(),
                request.efficiencyPercent(),
                json(basis), json(warnings), actorId);
        return IrrigationDtos.PlanResponse.from(plans.save(plan));
    }

    @Transactional
    public IrrigationDtos.PlanResponse approve(UUID tenantId, UUID actorId, UUID planId) {
        IrrigationPlanEntity plan = requirePlan(tenantId, planId);
        plan.approve(actorId);
        return IrrigationDtos.PlanResponse.from(plan);
    }

    @Transactional
    public IrrigationDtos.ScheduleResponse schedule(
            UUID tenantId, UUID planId, IrrigationDtos.ScheduleRequest request) {
        IrrigationPlanEntity plan = requirePlan(tenantId, planId);
        if (!plan.status().equals("APPROVED") && !plan.status().equals("ACTIVE")) {
            throw new BusinessException("IRRIGATION_PLAN_NOT_APPROVED",
                    "Scheduling requires an approved irrigation plan.", 422);
        }
        IrrigationScheduleEntity schedule = IrrigationScheduleEntity.create(
                plan.id(), plan.cropCycleId(), request.scheduledAt(),
                request.targetDepthMm(), request.plannedVolumeM3(),
                request.cropStageCode(), request.triggerType(),
                request.triggerThreshold());
        return IrrigationDtos.ScheduleResponse.from(schedules.save(schedule));
    }

    @Transactional
    public IrrigationDtos.ScheduleResponse defer(
            UUID tenantId, UUID scheduleId, IrrigationDtos.DeferRequest request) {
        IrrigationScheduleEntity schedule = requireSchedule(scheduleId);
        requireCycle(tenantId, schedule.cropCycleId());
        schedule.defer(request.newScheduledAt());
        return IrrigationDtos.ScheduleResponse.from(schedule);
    }

    @Transactional
    public IrrigationDtos.ScheduleResponse skip(
            UUID tenantId, UUID scheduleId, IrrigationDtos.SkipRequest request) {
        IrrigationScheduleEntity schedule = requireSchedule(scheduleId);
        requireCycle(tenantId, schedule.cropCycleId());
        schedule.skip(request.reason());
        return IrrigationDtos.ScheduleResponse.from(schedule);
    }

    @Transactional
    public UUID execute(UUID tenantId, UUID actorId, UUID scheduleId,
                        IrrigationDtos.ExecutionRequest request) {
        IrrigationScheduleEntity schedule = requireSchedule(scheduleId);
        CropCycleEntity cycle = requireCycle(tenantId, schedule.cropCycleId());
        ensureWritable(cycle);
        IrrigationPlanEntity plan = requirePlan(tenantId, schedule.irrigationPlanId());

        if (request.actualDepthMm() != null &&
                request.actualDepthMm().compareTo(new BigDecimal("1000")) > 0) {
            throw new BusinessException("IRRIGATION_DEPTH_IMPLAUSIBLE",
                    "Actual irrigation depth is implausibly high.", 422);
        }

        schedule.start();
        IrrigationExecutionEntity execution = executions.save(
                IrrigationExecutionEntity.create(
                        schedule.id(), cycle.id(), plan.waterSourceId(),
                        request.startedAt(), request.actualDepthMm(),
                        request.actualVolumeM3(), request.pumpRuntimeMinutes(),
                        request.energyConsumedKwh(),
                        request.executionStatus(), request.notes(), actorId));
        if ("COMPLETED".equals(request.executionStatus())) {
            schedule.execute();
        }
        recalculateAccounting(cycle.id(), plan.waterSourceId(),
                plan.seasonalWaterRequirementMm());
        return execution.id();
    }

    @Transactional(readOnly = true)
    public List<IrrigationDtos.PlanResponse> listPlans(UUID tenantId, UUID cycleId) {
        requireCycle(tenantId, cycleId);
        return plans.findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(tenantId, cycleId)
                .stream().map(IrrigationDtos.PlanResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<IrrigationDtos.ScheduleResponse> listSchedules(
            UUID tenantId, UUID cycleId) {
        requireCycle(tenantId, cycleId);
        return schedules.findByCropCycleIdOrderByScheduledAtAsc(cycleId)
                .stream().map(IrrigationDtos.ScheduleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public IrrigationDtos.AccountingResponse accounting(
            UUID tenantId, UUID cycleId, UUID waterSourceId) {
        requireCycle(tenantId, cycleId);
        return this.accounting.findByCropCycleIdAndWaterSourceId(cycleId, waterSourceId)
                .map(IrrigationDtos.AccountingResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WATER_ACCOUNTING_NOT_FOUND", "Water accounting not found."));
    }

    private void recalculateAccounting(UUID cycleId, UUID sourceId, BigDecimal demand) {
        accounting.deleteByCropCycleId(cycleId);
        BigDecimal appliedMm = executions.findByCropCycleId(cycleId).stream()
                .map(IrrigationExecutionEntity::actualDepthMm)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal volume = executions.findByCropCycleId(cycleId).stream()
                .map(IrrigationExecutionEntity::actualVolumeM3)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        accounting.save(WaterAccountingEntity.create(
                cycleId, sourceId, BigDecimal.ZERO,
                appliedMm, volume, demand));
    }

    private IrrigationPlanEntity requirePlan(UUID tenantId, UUID planId) {
        return plans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "IRRIGATION_PLAN_NOT_FOUND", "Irrigation plan not found."));
    }

    private IrrigationScheduleEntity requireSchedule(UUID scheduleId) {
        return schedules.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "IRRIGATION_SCHEDULE_NOT_FOUND", "Irrigation schedule not found."));
    }

    private CropCycleEntity requireCycle(UUID tenantId, UUID cycleId) {
        return cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
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
                    "Unable to serialize irrigation-plan data.", 500);
        }
    }
}
