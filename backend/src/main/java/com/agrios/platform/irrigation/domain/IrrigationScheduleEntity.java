package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "irrigation_schedule", schema = "irrigation")
public class IrrigationScheduleEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID irrigationPlanId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private Instant scheduledAt;
    @Column(nullable = false) private BigDecimal targetDepthMm;
    private BigDecimal plannedVolumeM3;
    private String cropStageCode;
    @Column(nullable = false) private String triggerType;
    private BigDecimal triggerThreshold;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String skipReason;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected IrrigationScheduleEntity() {}

    public static IrrigationScheduleEntity create(
            UUID planId, UUID cycleId, Instant scheduledAt,
            BigDecimal depth, BigDecimal volume, String stage,
            String trigger, BigDecimal threshold) {
        IrrigationScheduleEntity value = new IrrigationScheduleEntity();
        value.id = UUID.randomUUID();
        value.irrigationPlanId = planId;
        value.cropCycleId = cycleId;
        value.scheduledAt = scheduledAt;
        value.targetDepthMm = depth;
        value.plannedVolumeM3 = volume;
        value.cropStageCode = stage;
        value.triggerType = trigger;
        value.triggerThreshold = threshold;
        value.status = "SCHEDULED";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void defer(Instant newDate) {
        if (status.equals("EXECUTED") || status.equals("CANCELLED")) {
            throw new IllegalStateException("Schedule is terminal.");
        }
        scheduledAt = newDate;
        status = "DEFERRED";
        updatedAt = Instant.now();
    }

    public void skip(String reason) {
        if (status.equals("EXECUTED")) {
            throw new IllegalStateException("Executed schedule cannot be skipped.");
        }
        status = "SKIPPED";
        skipReason = reason;
        updatedAt = Instant.now();
    }

    public void start() {
        status = "IN_PROGRESS";
        updatedAt = Instant.now();
    }

    public void execute() {
        status = "EXECUTED";
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID irrigationPlanId() { return irrigationPlanId; }
    public UUID cropCycleId() { return cropCycleId; }
    public Instant scheduledAt() { return scheduledAt; }
    public BigDecimal targetDepthMm() { return targetDepthMm; }
    public BigDecimal plannedVolumeM3() { return plannedVolumeM3; }
    public String cropStageCode() { return cropStageCode; }
    public String triggerType() { return triggerType; }
    public BigDecimal triggerThreshold() { return triggerThreshold; }
    public String status() { return status; }
    public String skipReason() { return skipReason; }
    public long version() { return version; }
}
