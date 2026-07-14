package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "irrigation_execution", schema = "irrigation")
public class IrrigationExecutionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID irrigationScheduleId;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID waterSourceId;
    @Column(nullable = false) private Instant startedAt;
    private Instant completedAt;
    private BigDecimal actualDepthMm;
    private BigDecimal actualVolumeM3;
    private Integer pumpRuntimeMinutes;
    private BigDecimal energyConsumedKwh;
    @Column(nullable = false) private String executionStatus;
    @Column(columnDefinition = "text") private String notes;
    private UUID executedBy;
    @Column(nullable = false) private Instant createdAt;

    protected IrrigationExecutionEntity() {}

    public static IrrigationExecutionEntity create(
            UUID scheduleId, UUID cycleId, UUID waterSourceId,
            Instant startedAt, BigDecimal depth, BigDecimal volume,
            Integer runtime, BigDecimal energy, String status,
            String notes, UUID actorId) {
        IrrigationExecutionEntity value = new IrrigationExecutionEntity();
        value.id = UUID.randomUUID();
        value.irrigationScheduleId = scheduleId;
        value.cropCycleId = cycleId;
        value.waterSourceId = waterSourceId;
        value.startedAt = startedAt;
        value.completedAt = status.equals("COMPLETED") ? Instant.now() : null;
        value.actualDepthMm = depth;
        value.actualVolumeM3 = volume;
        value.pumpRuntimeMinutes = runtime;
        value.energyConsumedKwh = energy;
        value.executionStatus = status;
        value.notes = notes;
        value.executedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal actualDepthMm() { return actualDepthMm; }
    public BigDecimal actualVolumeM3() { return actualVolumeM3; }
}
