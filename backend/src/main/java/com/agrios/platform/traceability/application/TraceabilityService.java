package com.agrios.platform.traceability.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.harvest.domain.*;
import com.agrios.platform.traceability.api.TraceabilityDtos;
import com.agrios.platform.traceability.domain.*;
import com.agrios.platform.yieldquality.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TraceabilityService {
    private final TraceLotRepository lots;
    private final PackageRepository packages;
    private final HarvestBatchRepository batches;
    private final QualityAssessmentRepository assessments;
    private final ObjectMapper mapper;

    public TraceabilityService(TraceLotRepository lots,
                               PackageRepository packages,
                               HarvestBatchRepository batches,
                               QualityAssessmentRepository assessments,
                               ObjectMapper mapper) {
        this.lots = lots;
        this.packages = packages;
        this.batches = batches;
        this.assessments = assessments;
        this.mapper = mapper;
    }

    @Transactional
    public TraceabilityDtos.LotResponse createLot(
            UUID tenantId, TraceabilityDtos.LotRequest request) {
        HarvestBatchEntity batch = batches.findByIdAndTenantId(
                        request.harvestBatchId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HARVEST_BATCH_NOT_FOUND", "Harvest batch not found."));

        if (request.quantity().compareTo(batch.netQuantity()) > 0) {
            throw new BusinessException("LOT_QUANTITY_EXCEEDS_BATCH",
                    "Lot quantity exceeds harvest batch quantity.", 422);
        }

        if (request.qualityAssessmentId() != null) {
            assessments.findByIdAndTenantId(
                    request.qualityAssessmentId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QUALITY_ASSESSMENT_NOT_FOUND",
                            "Quality assessment not found."));
        }

        String qrToken = UUID.randomUUID().toString().replace("-", "");
        Map<String,Object> snapshot = new LinkedHashMap<>();
        snapshot.put("harvestBatchId", batch.id());
        snapshot.put("cropCycleId", batch.cropCycleId());
        snapshot.put("fieldId", batch.fieldId());
        snapshot.put("harvestedAt", batch.harvestedAt());
        snapshot.put("netQuantity", batch.netQuantity());
        snapshot.put("quantityUnit", batch.quantityUnit());
        snapshot.put("qualityGrade", request.qualityGrade());

        TraceLotEntity lot = TraceLotEntity.create(
                tenantId, request.lotCode(), batch.id(),
                batch.cropCycleId(), batch.fieldId(),
                request.qualityAssessmentId(), request.qualityGrade(),
                request.quantity(), request.quantityUnit(),
                request.expiryDate(), qrToken, json(snapshot));
        return TraceabilityDtos.LotResponse.from(lots.save(lot));
    }

    @Transactional
    public UUID pack(UUID tenantId, UUID lotId,
                     TraceabilityDtos.PackageRequest request) {
        TraceLotEntity lot = requireLot(tenantId, lotId);
        if (request.packedQuantity().compareTo(lot.quantity()) > 0) {
            throw new BusinessException("PACKAGE_QUANTITY_EXCEEDS_LOT",
                    "Package quantity exceeds lot quantity.", 422);
        }
        PackageEntity packageEntity = packages.save(PackageEntity.create(
                tenantId, lot.id(), request.packageCode(),
                request.packageType(), request.packedQuantity(),
                request.quantityUnit(), request.bestBeforeDate()));
        lot.markPacked();
        batches.findByIdAndTenantId(lot.harvestBatchId(), tenantId)
                .ifPresent(batch -> batch.markPacked(
                        UUID.nameUUIDFromBytes("system".getBytes())));
        return packageEntity.id();
    }

    @Transactional(readOnly = true)
    public TraceabilityDtos.LotResponse publicTrace(String qrToken) {
        return lots.findByQrToken(qrToken)
                .map(TraceabilityDtos.LotResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TRACE_LOT_NOT_FOUND", "Traceability lot not found."));
    }

    @Transactional(readOnly = true)
    public List<TraceabilityDtos.LotResponse> listByCycle(
            UUID tenantId, UUID cycleId) {
        return lots.findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(
                        tenantId, cycleId)
                .stream().map(TraceabilityDtos.LotResponse::from).toList();
    }

    private TraceLotEntity requireLot(UUID tenantId, UUID lotId) {
        return lots.findByIdAndTenantId(lotId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TRACE_LOT_NOT_FOUND", "Traceability lot not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize traceability snapshot.", 500);
        }
    }
}
