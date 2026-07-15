package com.agrios.platform.traceability.api;

import com.agrios.platform.traceability.domain.TraceLotEntity;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class TraceabilityDtos {
    private TraceabilityDtos() {}

    public record LotRequest(
            @NotBlank String lotCode,
            @NotNull UUID harvestBatchId,
            UUID qualityAssessmentId,
            String qualityGrade,
            @NotNull @DecimalMin("0.0001") BigDecimal quantity,
            @NotBlank String quantityUnit,
            LocalDate expiryDate) {}

    public record PackageRequest(
            @NotBlank String packageCode,
            @NotBlank String packageType,
            @NotNull @DecimalMin("0.0001") BigDecimal packedQuantity,
            @NotBlank String quantityUnit,
            LocalDate bestBeforeDate) {}

    public record LotResponse(
            UUID id, String lotCode, UUID harvestBatchId,
            UUID cropCycleId, UUID fieldId, String qualityGrade,
            BigDecimal quantity, String quantityUnit,
            String status, String qrToken,
            String traceSnapshotJson, long version) {
        public static LotResponse from(TraceLotEntity value) {
            return new LotResponse(value.id(), value.lotCode(),
                    value.harvestBatchId(), value.cropCycleId(),
                    value.fieldId(), value.qualityGrade(),
                    value.quantity(), value.quantityUnit(),
                    value.status(), value.qrToken(),
                    value.traceSnapshot(), value.version());
        }
    }
}
