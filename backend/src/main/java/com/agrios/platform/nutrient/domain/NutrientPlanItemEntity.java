package com.agrios.platform.nutrient.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "nutrient_plan_item", schema = "nutrient")
public class NutrientPlanItemEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID nutrientPlanId;
    @Column(nullable = false) private String nutrientCode;
    @Column(nullable = false) private String sourceCategory;
    @Column(nullable = false) private String sourceName;
    @Column(nullable = false) private BigDecimal plannedQuantity;
    @Column(nullable = false) private String quantityUnit;
    private BigDecimal nutrientContentPercent;
    private BigDecimal plannedNutrientQuantity;
    private LocalDate plannedApplicationDate;
    private String cropStageCode;
    @Column(nullable = false) private String applicationMethod;
    private Integer splitNo;
    private Integer safetyIntervalDays;
    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false) private Instant createdAt;

    protected NutrientPlanItemEntity() {}

    public static NutrientPlanItemEntity create(
            UUID planId, String nutrientCode, String sourceCategory,
            String sourceName, BigDecimal quantity, String unit,
            BigDecimal contentPercent, BigDecimal nutrientQuantity,
            LocalDate applicationDate, String stage, String method,
            Integer splitNo, Integer safetyIntervalDays, String notes) {
        NutrientPlanItemEntity value = new NutrientPlanItemEntity();
        value.id = UUID.randomUUID();
        value.nutrientPlanId = planId;
        value.nutrientCode = nutrientCode;
        value.sourceCategory = sourceCategory;
        value.sourceName = sourceName;
        value.plannedQuantity = quantity;
        value.quantityUnit = unit;
        value.nutrientContentPercent = contentPercent;
        value.plannedNutrientQuantity = nutrientQuantity;
        value.plannedApplicationDate = applicationDate;
        value.cropStageCode = stage;
        value.applicationMethod = method;
        value.splitNo = splitNo;
        value.safetyIntervalDays = safetyIntervalDays;
        value.notes = notes;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID nutrientPlanId() { return nutrientPlanId; }
    public String nutrientCode() { return nutrientCode; }
    public String sourceCategory() { return sourceCategory; }
    public String sourceName() { return sourceName; }
    public BigDecimal plannedQuantity() { return plannedQuantity; }
    public String quantityUnit() { return quantityUnit; }
    public BigDecimal plannedNutrientQuantity() { return plannedNutrientQuantity; }
    public LocalDate plannedApplicationDate() { return plannedApplicationDate; }
    public String cropStageCode() { return cropStageCode; }
    public String applicationMethod() { return applicationMethod; }
}
