package com.agrios.platform.harvest.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "harvest_batch", schema = "harvest")
public class HarvestBatchEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID harvestPlanId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private String batchCode;
    @Column(nullable = false) private Instant harvestedAt;
    @Column(nullable = false) private String harvestMethod;
    @Column(nullable = false) private BigDecimal grossQuantity;
    @Column(nullable = false) private BigDecimal tareQuantity;
    @Column(nullable = false) private BigDecimal netQuantity;
    @Column(nullable = false) private String quantityUnit;
    private BigDecimal moisturePercent;
    @Column(nullable = false) private BigDecimal damagedQuantity;
    @Column(nullable = false) private BigDecimal fieldLossQuantity;
    @Column(nullable = false) private BigDecimal transportLossQuantity;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String notes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected HarvestBatchEntity() {}

    public static HarvestBatchEntity create(
            UUID tenantId, UUID planId, UUID cycleId, UUID fieldId,
            String batchCode, Instant harvestedAt, String method,
            BigDecimal gross, BigDecimal tare, String unit,
            BigDecimal moisture, BigDecimal damaged,
            BigDecimal fieldLoss, BigDecimal transportLoss,
            String notes, UUID actorId) {
        HarvestBatchEntity value = new HarvestBatchEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.harvestPlanId = planId;
        value.cropCycleId = cycleId;
        value.fieldId = fieldId;
        value.batchCode = batchCode;
        value.harvestedAt = harvestedAt;
        value.harvestMethod = method;
        value.grossQuantity = gross;
        value.tareQuantity = tare == null ? BigDecimal.ZERO : tare;
        value.netQuantity = gross.subtract(value.tareQuantity);
        value.quantityUnit = unit;
        value.moisturePercent = moisture;
        value.damagedQuantity = damaged == null ? BigDecimal.ZERO : damaged;
        value.fieldLossQuantity = fieldLoss == null ? BigDecimal.ZERO : fieldLoss;
        value.transportLossQuantity = transportLoss == null ? BigDecimal.ZERO : transportLoss;
        value.status = "HARVESTED";
        value.notes = notes;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void moveToQualityCheck(UUID actorId) {
        status = "UNDER_QUALITY_CHECK";
        touch(actorId);
    }

    public void markGraded(UUID actorId) {
        status = "GRADED";
        touch(actorId);
    }

    public void markPacked(UUID actorId) {
        status = "PACKED";
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID harvestPlanId() { return harvestPlanId; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID fieldId() { return fieldId; }
    public String batchCode() { return batchCode; }
    public Instant harvestedAt() { return harvestedAt; }
    public BigDecimal grossQuantity() { return grossQuantity; }
    public BigDecimal tareQuantity() { return tareQuantity; }
    public BigDecimal netQuantity() { return netQuantity; }
    public String quantityUnit() { return quantityUnit; }
    public BigDecimal moisturePercent() { return moisturePercent; }
    public BigDecimal damagedQuantity() { return damagedQuantity; }
    public BigDecimal fieldLossQuantity() { return fieldLossQuantity; }
    public BigDecimal transportLossQuantity() { return transportLossQuantity; }
    public String status() { return status; }
    public long version() { return version; }
}
