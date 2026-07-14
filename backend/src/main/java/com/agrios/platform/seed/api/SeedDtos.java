package com.agrios.platform.seed.api;

import com.agrios.platform.seed.domain.SeedLotEntity;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public final class SeedDtos {
    private SeedDtos() {}

    public record CreateLotRequest(
            @NotNull UUID cropId,
            UUID varietyId,
            @NotBlank String lotCode,
            @NotBlank String seedClass,
            String supplierName,
            String producerName,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal germinationPercent,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal purityPercent,
            @NotNull @DecimalMin("0.0001") BigDecimal quantityAvailable,
            @NotBlank String quantityUnit,
            LocalDate packedOn,
            LocalDate expiresOn) {
        @AssertTrue(message = "expiresOn must not be before packedOn")
        public boolean validDates() {
            return packedOn == null || expiresOn == null || !expiresOn.isBefore(packedOn);
        }
    }

    public record GerminationRequest(
            @NotNull Instant testedAt,
            @Min(1) int sampleSize,
            @Min(0) int germinatedCount,
            @NotBlank String method,
            String notes) {
        @AssertTrue(message = "germinatedCount must not exceed sampleSize")
        public boolean validCount() { return germinatedCount <= sampleSize; }
    }

    public record TreatmentRequest(
            @NotBlank String treatmentType,
            String productName,
            @NotNull LocalDate treatmentDate,
            @NotNull @DecimalMin("0.0001") BigDecimal treatedQuantity,
            @NotBlank String quantityUnit,
            String notes) {}

    public record AllocationRequest(
            @NotNull UUID cropCycleId,
            @NotNull @DecimalMin("0.0001") BigDecimal allocatedQuantity,
            @NotBlank String quantityUnit) {}

    public record LotResponse(
            UUID id, UUID cropId, UUID varietyId, String lotCode,
            String seedClass, String supplierName, String producerName,
            BigDecimal germinationPercent, BigDecimal purityPercent,
            String treatmentStatus, BigDecimal quantityAvailable,
            String quantityUnit, LocalDate packedOn, LocalDate expiresOn,
            String status, long version) {
        public static LotResponse from(SeedLotEntity value) {
            return new LotResponse(value.id(), value.cropId(), value.varietyId(),
                    value.lotCode(), value.seedClass(), value.supplierName(),
                    value.producerName(), value.germinationPercent(),
                    value.purityPercent(), value.treatmentStatus(),
                    value.quantityAvailable(), value.quantityUnit(),
                    value.packedOn(), value.expiresOn(),
                    value.status(), value.version());
        }
    }
}
