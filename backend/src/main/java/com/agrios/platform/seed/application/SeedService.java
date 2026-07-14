package com.agrios.platform.seed.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.knowledge.domain.*;
import com.agrios.platform.seed.api.SeedDtos;
import com.agrios.platform.seed.domain.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeedService {
    private final SeedLotRepository lots;
    private final GerminationTestRepository germinationTests;
    private final SeedTreatmentRepository treatments;
    private final SeedAllocationRepository allocations;
    private final CropCycleRepository cycles;
    private final CropRepository crops;
    private final VarietyRepository varieties;

    public SeedService(SeedLotRepository lots,
                       GerminationTestRepository germinationTests,
                       SeedTreatmentRepository treatments,
                       SeedAllocationRepository allocations,
                       CropCycleRepository cycles,
                       CropRepository crops,
                       VarietyRepository varieties) {
        this.lots = lots;
        this.germinationTests = germinationTests;
        this.treatments = treatments;
        this.allocations = allocations;
        this.cycles = cycles;
        this.crops = crops;
        this.varieties = varieties;
    }

    @Transactional
    public SeedDtos.LotResponse createLot(UUID tenantId, UUID actorId,
                                          SeedDtos.CreateLotRequest request) {
        CropEntity crop = crops.findByIdAndTenantId(request.cropId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_NOT_FOUND", "Crop not found."));
        if (request.varietyId() != null) {
            VarietyEntity variety = varieties.findByIdAndTenantId(request.varietyId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "VARIETY_NOT_FOUND", "Variety not found."));
            if (!variety.cropId().equals(crop.id())) {
                throw new ConflictException("VARIETY_CROP_MISMATCH",
                        "Variety does not belong to crop.");
            }
        }
        SeedLotEntity lot = SeedLotEntity.create(
                tenantId, crop.id(), request.varietyId(), request.lotCode(),
                request.seedClass(), request.supplierName(), request.producerName(),
                request.germinationPercent(), request.purityPercent(),
                request.quantityAvailable(), request.quantityUnit(),
                request.packedOn(), request.expiresOn(), actorId);
        return SeedDtos.LotResponse.from(lots.save(lot));
    }

    @Transactional
    public SeedDtos.LotResponse germinationTest(UUID tenantId, UUID actorId,
                                                UUID lotId,
                                                SeedDtos.GerminationRequest request) {
        SeedLotEntity lot = requireLot(tenantId, lotId);
        GerminationTestEntity test = GerminationTestEntity.create(
                lot.id(), request.testedAt(), request.sampleSize(),
                request.germinatedCount(), request.method(),
                request.notes(), actorId);
        germinationTests.save(test);
        lot.updateGermination(test.germinationPercent(), actorId);
        return SeedDtos.LotResponse.from(lot);
    }

    @Transactional
    public SeedDtos.LotResponse treat(UUID tenantId, UUID actorId,
                                      UUID lotId,
                                      SeedDtos.TreatmentRequest request) {
        SeedLotEntity lot = requireLot(tenantId, lotId);
        if (lot.quantityAvailable().compareTo(request.treatedQuantity()) < 0) {
            throw new BusinessException("SEED_TREATMENT_QUANTITY_EXCEEDED",
                    "Treatment quantity exceeds available seed.", 422);
        }
        treatments.save(SeedTreatmentEntity.create(
                lot.id(), request.treatmentType(), request.productName(),
                request.treatmentDate(), request.treatedQuantity(),
                request.quantityUnit(), request.notes(), actorId));
        lot.markTreated(actorId);
        return SeedDtos.LotResponse.from(lot);
    }

    @Transactional
    public SeedDtos.LotResponse allocate(UUID tenantId, UUID actorId,
                                         UUID lotId,
                                         SeedDtos.AllocationRequest request) {
        SeedLotEntity lot = requireLot(tenantId, lotId);
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        if (!cycle.cropId().equals(lot.cropId())) {
            throw new ConflictException("SEED_CROP_MISMATCH",
                    "Seed lot crop does not match crop cycle.");
        }
        if (lot.varietyId() != null && cycle.varietyId() != null &&
                !lot.varietyId().equals(cycle.varietyId())) {
            throw new ConflictException("SEED_VARIETY_MISMATCH",
                    "Seed lot variety does not match crop cycle.");
        }
        if (allocations.existsBySeedLotIdAndCropCycleId(lot.id(), cycle.id())) {
            throw new ConflictException("SEED_ALREADY_ALLOCATED",
                    "This seed lot is already allocated to the crop cycle.");
        }
        try {
            lot.allocate(request.allocatedQuantity(), actorId);
        } catch (IllegalStateException ex) {
            throw new BusinessException("SEED_QUANTITY_INSUFFICIENT", ex.getMessage(), 422);
        }
        allocations.save(SeedAllocationEntity.create(
                lot.id(), cycle.id(), request.allocatedQuantity(),
                request.quantityUnit(), actorId));
        return SeedDtos.LotResponse.from(lot);
    }

    @Transactional(readOnly = true)
    public List<SeedDtos.LotResponse> availableLots(UUID tenantId, UUID cropId) {
        return lots.findByTenantIdAndCropIdAndStatusOrderByCreatedAtDesc(
                        tenantId, cropId, "AVAILABLE")
                .stream().map(SeedDtos.LotResponse::from).toList();
    }

    private SeedLotEntity requireLot(UUID tenantId, UUID lotId) {
        return lots.findByIdAndTenantId(lotId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SEED_LOT_NOT_FOUND", "Seed lot not found."));
    }
}
