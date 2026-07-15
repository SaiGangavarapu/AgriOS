package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ipm_execution", schema = "crophealth")
public class IpmExecutionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID ipmPlanId;
    @Column(nullable = false) private UUID ipmActionId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private Instant executedAt;
    private BigDecimal executedQuantity;
    private String quantityUnit;
    private BigDecimal coveredAreaHectares;
    @Column(nullable = false) private String weatherCheckStatus;
    @Column(nullable = false) private String safetyValidationStatus;
    private String observedResult;
    @Column(columnDefinition = "text") private String notes;
    private UUID executedBy;
    @Column(nullable = false) private Instant createdAt;

    protected IpmExecutionEntity() {}

    public static IpmExecutionEntity create(
            UUID planId, UUID actionId, UUID cycleId,
            Instant executedAt, BigDecimal quantity, String unit,
            BigDecimal area, String weatherStatus,
            String safetyStatus, String observedResult,
            String notes, UUID actorId) {
        IpmExecutionEntity value = new IpmExecutionEntity();
        value.id = UUID.randomUUID();
        value.ipmPlanId = planId;
        value.ipmActionId = actionId;
        value.cropCycleId = cycleId;
        value.executedAt = executedAt;
        value.executedQuantity = quantity;
        value.quantityUnit = unit;
        value.coveredAreaHectares = area;
        value.weatherCheckStatus = weatherStatus;
        value.safetyValidationStatus = safetyStatus;
        value.observedResult = observedResult;
        value.notes = notes;
        value.executedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
