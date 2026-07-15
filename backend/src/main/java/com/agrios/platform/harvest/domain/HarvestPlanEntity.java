package com.agrios.platform.harvest.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "harvest_plan", schema = "harvest")
public class HarvestPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private LocalDate expectedStartDate;
    @Column(nullable = false) private LocalDate expectedEndDate;
    @Column(nullable = false) private String harvestMethod;
    private BigDecimal expectedYieldQuantity;
    private String expectedYieldUnit;
    private BigDecimal readinessScore;
    @Column(nullable = false) private String weatherSuitability;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String notes;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected HarvestPlanEntity() {}

    public static HarvestPlanEntity create(
            UUID tenantId, UUID cycleId, UUID fieldId,
            LocalDate start, LocalDate end, String method,
            BigDecimal expectedYield, String unit,
            BigDecimal readiness, String weatherSuitability,
            String notes, UUID actorId) {
        HarvestPlanEntity value = new HarvestPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cycleId;
        value.fieldId = fieldId;
        value.expectedStartDate = start;
        value.expectedEndDate = end;
        value.harvestMethod = method;
        value.expectedYieldQuantity = expectedYield;
        value.expectedYieldUnit = unit;
        value.readinessScore = readiness;
        value.weatherSuitability = weatherSuitability;
        value.status = "PLANNED";
        value.notes = notes;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("PLANNED")) throw new IllegalStateException("Harvest plan is not planned.");
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        touch(actorId);
    }

    public void start(UUID actorId) {
        if (!status.equals("APPROVED") && !status.equals("READY")) {
            throw new IllegalStateException("Harvest plan cannot start.");
        }
        status = "IN_PROGRESS";
        touch(actorId);
    }

    public void complete(UUID actorId) {
        if (!status.equals("IN_PROGRESS")) throw new IllegalStateException("Harvest plan is not in progress.");
        status = "COMPLETED";
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID fieldId() { return fieldId; }
    public LocalDate expectedStartDate() { return expectedStartDate; }
    public LocalDate expectedEndDate() { return expectedEndDate; }
    public String harvestMethod() { return harvestMethod; }
    public BigDecimal expectedYieldQuantity() { return expectedYieldQuantity; }
    public String expectedYieldUnit() { return expectedYieldUnit; }
    public BigDecimal readinessScore() { return readinessScore; }
    public String weatherSuitability() { return weatherSuitability; }
    public String status() { return status; }
    public long version() { return version; }
}
