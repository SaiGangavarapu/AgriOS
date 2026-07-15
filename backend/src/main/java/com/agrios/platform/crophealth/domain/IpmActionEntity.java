package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "ipm_action", schema = "crophealth")
public class IpmActionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID ipmPlanId;
    @Column(nullable = false) private int actionSequence;
    @Column(nullable = false) private String actionCategory;
    @Column(nullable = false) private String actionName;
    @Column(columnDefinition = "text") private String actionDescription;
    private String triggerCondition;
    private String cropStageCode;
    private LocalDate plannedDate;
    private String productName;
    private BigDecimal dosage;
    private String dosageUnit;
    private String applicationMethod;
    private Integer preHarvestIntervalDays;
    private Integer reentryIntervalHours;
    @Column(nullable = false) private boolean pollinatorWarning;
    private BigDecimal waterBodyBufferMeters;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected IpmActionEntity() {}

    public static IpmActionEntity create(
            UUID planId, int sequence, String category, String name,
            String description, String triggerCondition,
            String cropStageCode, LocalDate plannedDate,
            String productName, BigDecimal dosage, String dosageUnit,
            String applicationMethod, Integer phiDays,
            Integer reentryHours, boolean pollinatorWarning,
            BigDecimal waterBuffer) {
        IpmActionEntity value = new IpmActionEntity();
        value.id = UUID.randomUUID();
        value.ipmPlanId = planId;
        value.actionSequence = sequence;
        value.actionCategory = category;
        value.actionName = name;
        value.actionDescription = description;
        value.triggerCondition = triggerCondition;
        value.cropStageCode = cropStageCode;
        value.plannedDate = plannedDate;
        value.productName = productName;
        value.dosage = dosage;
        value.dosageUnit = dosageUnit;
        value.applicationMethod = applicationMethod;
        value.preHarvestIntervalDays = phiDays;
        value.reentryIntervalHours = reentryHours;
        value.pollinatorWarning = pollinatorWarning;
        value.waterBodyBufferMeters = waterBuffer;
        value.status = "PLANNED";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String actionCategory() { return actionCategory; }
    public String status() { return status; }
}
