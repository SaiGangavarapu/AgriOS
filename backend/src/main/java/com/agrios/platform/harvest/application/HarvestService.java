package com.agrios.platform.harvest.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.farm.domain.FieldRepository;
import com.agrios.platform.harvest.api.HarvestDtos;
import com.agrios.platform.harvest.domain.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HarvestService {
    private final HarvestPlanRepository plans;
    private final HarvestBatchRepository batches;
    private final CropCycleRepository cycles;
    private final FieldRepository fields;

    public HarvestService(HarvestPlanRepository plans,
                          HarvestBatchRepository batches,
                          CropCycleRepository cycles,
                          FieldRepository fields) {
        this.plans = plans;
        this.batches = batches;
        this.cycles = cycles;
        this.fields = fields;
    }

    @Transactional
    public HarvestDtos.PlanResponse createPlan(
            UUID tenantId, UUID actorId, HarvestDtos.PlanRequest request) {
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        if (!cycle.fieldId().equals(request.fieldId())) {
            throw new ConflictException("HARVEST_FIELD_CYCLE_MISMATCH",
                    "Crop cycle does not belong to field.");
        }
        HarvestPlanEntity plan = HarvestPlanEntity.create(
                tenantId, cycle.id(), request.fieldId(),
                request.expectedStartDate(), request.expectedEndDate(),
                request.harvestMethod(), request.expectedYieldQuantity(),
                request.expectedYieldUnit(), request.readinessScore(),
                request.weatherSuitability(), request.notes(), actorId);
        return HarvestDtos.PlanResponse.from(plans.save(plan));
    }

    @Transactional
    public HarvestDtos.PlanResponse approve(UUID tenantId, UUID actorId, UUID planId) {
        HarvestPlanEntity plan = requirePlan(tenantId, planId);
        plan.approve(actorId);
        return HarvestDtos.PlanResponse.from(plan);
    }

    @Transactional
    public HarvestDtos.BatchResponse createBatch(
            UUID tenantId, UUID actorId, UUID planId,
            HarvestDtos.BatchRequest request) {
        HarvestPlanEntity plan = requirePlan(tenantId, planId);
        if (!plan.status().equals("APPROVED") && !plan.status().equals("IN_PROGRESS")) {
            throw new BusinessException("HARVEST_PLAN_NOT_APPROVED",
                    "Harvest batch requires an approved harvest plan.", 422);
        }
        if (plan.status().equals("APPROVED")) plan.start(actorId);

        HarvestBatchEntity batch = HarvestBatchEntity.create(
                tenantId, plan.id(), plan.cropCycleId(),
                plan.fieldId(), request.batchCode(),
                request.harvestedAt(), request.harvestMethod(),
                request.grossQuantity(), request.tareQuantity(),
                request.quantityUnit(), request.moisturePercent(),
                request.damagedQuantity(), request.fieldLossQuantity(),
                request.transportLossQuantity(), request.notes(), actorId);
        return HarvestDtos.BatchResponse.from(batches.save(batch));
    }

    @Transactional(readOnly = true)
    public List<HarvestDtos.BatchResponse> listBatches(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return batches.findByTenantIdAndCropCycleIdOrderByHarvestedAtDesc(tenantId, cycleId)
                .stream().map(HarvestDtos.BatchResponse::from).toList();
    }

    private HarvestPlanEntity requirePlan(UUID tenantId, UUID planId) {
        return plans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HARVEST_PLAN_NOT_FOUND", "Harvest plan not found."));
    }
}
