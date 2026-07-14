package com.agrios.platform.cropcycle.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.api.CropCycleDtos;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.cropplanning.domain.*;
import com.agrios.platform.farm.domain.FieldRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropCycleService {
    private final CropCycleRepository cycles;
    private final StageObservationRepository stages;
    private final CropLossRepository losses;
    private final SeasonClosureRepository closures;
    private final CropPlanRepository plans;
    private final FieldRepository fields;

    public CropCycleService(CropCycleRepository cycles,
                            StageObservationRepository stages,
                            CropLossRepository losses,
                            SeasonClosureRepository closures,
                            CropPlanRepository plans,
                            FieldRepository fields) {
        this.cycles = cycles;
        this.stages = stages;
        this.losses = losses;
        this.closures = closures;
        this.plans = plans;
        this.fields = fields;
    }

    @Transactional
    public CropCycleDtos.Response create(UUID tenantId, UUID actorId,
                                         CropCycleDtos.CreateRequest request) {
        CropPlanEntity plan = plans.findByIdAndTenantId(request.cropPlanId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_PLAN_NOT_FOUND", "Crop plan not found."));
        if (!"APPROVED".equals(plan.status())) {
            throw new BusinessException("CROP_PLAN_NOT_APPROVED",
                    "Only an approved crop plan can create a crop cycle.", 422);
        }
        cycles.findByCropPlanIdAndTenantId(plan.id(), tenantId).ifPresent(existing -> {
            throw new ConflictException("CROP_CYCLE_ALREADY_EXISTS",
                    "A crop cycle already exists for this plan.");
        });

        CropCycleEntity cycle = CropCycleEntity.create(
                tenantId, plan.id(), plan.fieldId(),
                plan.selectedCropId(), plan.selectedVarietyId(),
                plan.seasonCode(), plan.plannedAreaHectares(), actorId);
        return CropCycleDtos.Response.from(cycles.save(cycle));
    }

    @Transactional
    public CropCycleDtos.Response activate(UUID tenantId, UUID actorId, UUID cycleId) {
        CropCycleEntity cycle = requireCycle(tenantId, cycleId);
        cycle.activate(actorId);
        return CropCycleDtos.Response.from(cycle);
    }

    @Transactional
    public CropCycleDtos.Response recordSowing(UUID tenantId, UUID actorId,
                                               UUID cycleId,
                                               CropCycleDtos.SowingRequest request) {
        CropCycleEntity cycle = requireCycle(tenantId, cycleId);
        cycle.recordSowing(request.sowingDate(), actorId);
        stages.save(StageObservationEntity.create(
                cycle.id(), "SOWN", request.sowingDate().atStartOfDay(
                        java.time.ZoneOffset.UTC).toInstant(),
                "FARMER", java.math.BigDecimal.ONE, null, actorId));
        return CropCycleDtos.Response.from(cycle);
    }

    @Transactional
    public CropCycleDtos.Response updateStage(UUID tenantId, UUID actorId,
                                              UUID cycleId,
                                              CropCycleDtos.StageRequest request) {
        CropCycleEntity cycle = requireCycle(tenantId, cycleId);
        cycle.updateStage(request.stageCode(), request.lifecycleStatus(), actorId);
        stages.save(StageObservationEntity.create(
                cycle.id(), request.stageCode(), request.observedAt(),
                request.sourceType(), request.confidenceScore(),
                request.notes(), actorId));
        return CropCycleDtos.Response.from(cycle);
    }

    @Transactional
    public CropCycleDtos.Response recordLoss(UUID tenantId, UUID actorId,
                                             UUID cycleId,
                                             CropCycleDtos.LossRequest request) {
        CropCycleEntity cycle = requireCycle(tenantId, cycleId);
        if (request.affectedAreaHectares().compareTo(cycle.plannedAreaHectares()) > 0) {
            throw new BusinessException("LOSS_AREA_EXCEEDS_CROP_AREA",
                    "Affected area exceeds crop-cycle area.", 422);
        }
        losses.save(CropLossEntity.create(
                cycle.id(), request.lossType(), request.affectedAreaHectares(),
                request.estimatedLossPercent(), request.causeCode(),
                request.observedAt(), request.notes(), actorId));
        cycle.registerLoss(request.totalLoss(), actorId);
        return CropCycleDtos.Response.from(cycle);
    }

    @Transactional
    public CropCycleDtos.Response close(UUID tenantId, UUID actorId,
                                        UUID cycleId,
                                        CropCycleDtos.CloseRequest request) {
        CropCycleEntity cycle = requireCycle(tenantId, cycleId);
        if (closures.findByCropCycleId(cycleId).isPresent()) {
            throw new ConflictException("SEASON_ALREADY_CLOSED",
                    "Season closure already exists.");
        }
        closures.save(SeasonClosureEntity.create(
                cycle.id(), request.harvestedQuantity(), request.harvestedUnit(),
                request.marketableQuantity(), request.retainedSeedQuantity(),
                request.householdUseQuantity(), request.closingNotes(), actorId));
        cycle.close(request.closingNotes(), actorId);
        return CropCycleDtos.Response.from(cycle);
    }

    @Transactional(readOnly = true)
    public CropCycleDtos.Response get(UUID tenantId, UUID cycleId) {
        return CropCycleDtos.Response.from(requireCycle(tenantId, cycleId));
    }

    @Transactional(readOnly = true)
    public List<CropCycleDtos.Response> listByField(UUID tenantId, UUID fieldId) {
        fields.findByIdAndTenantId(fieldId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        return cycles.findByTenantIdAndFieldIdOrderByCreatedAtDesc(tenantId, fieldId)
                .stream().map(CropCycleDtos.Response::from).toList();
    }

    private CropCycleEntity requireCycle(UUID tenantId, UUID cycleId) {
        return cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
    }
}
