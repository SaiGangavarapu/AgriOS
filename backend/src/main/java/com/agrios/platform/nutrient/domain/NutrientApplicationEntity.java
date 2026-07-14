package com.agrios.platform.nutrient.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nutrient_application", schema = "nutrient")
public class NutrientApplicationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID nutrientPlanId;
    private UUID nutrientPlanItemId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private Instant appliedAt;
    @Column(nullable = false) private String sourceCategory;
    @Column(nullable = false) private String sourceName;
    @Column(nullable = false) private BigDecimal appliedQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private String applicationMethod;
    private BigDecimal areaHectares;
    @Column(nullable = false) private String weatherCheckStatus;
    @Column(nullable = false) private String safetyValidationStatus;
    @Column(columnDefinition = "text") private String notes;
    private UUID appliedBy;
    @Column(nullable = false) private Instant createdAt;

    protected NutrientApplicationEntity() {}

    public static NutrientApplicationEntity create(
            UUID planId, UUID itemId, UUID cycleId, Instant appliedAt,
            String sourceCategory, String sourceName, BigDecimal quantity,
            String unit, String method, BigDecimal area,
            String weatherStatus, String safetyStatus,
            String notes, UUID actorId) {
        NutrientApplicationEntity value = new NutrientApplicationEntity();
        value.id = UUID.randomUUID();
        value.nutrientPlanId = planId;
        value.nutrientPlanItemId = itemId;
        value.cropCycleId = cycleId;
        value.appliedAt = appliedAt;
        value.sourceCategory = sourceCategory;
        value.sourceName = sourceName;
        value.appliedQuantity = quantity;
        value.quantityUnit = unit;
        value.applicationMethod = method;
        value.areaHectares = area;
        value.weatherCheckStatus = weatherStatus;
        value.safetyValidationStatus = safetyStatus;
        value.notes = notes;
        value.appliedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal appliedQuantity() { return appliedQuantity; }
}
