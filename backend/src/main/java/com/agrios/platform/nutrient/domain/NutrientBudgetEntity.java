package com.agrios.platform.nutrient.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nutrient_budget", schema = "nutrient")
public class NutrientBudgetEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private String nutrientCode;
    @Column(nullable = false) private BigDecimal plannedQuantity;
    @Column(nullable = false) private BigDecimal appliedQuantity;
    private BigDecimal estimatedCropUptake;
    private BigDecimal estimatedBalance;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private Instant calculatedAt;
    @Column(nullable = false) private String calculationVersion;

    protected NutrientBudgetEntity() {}

    public static NutrientBudgetEntity create(UUID cycleId, String code,
                                              BigDecimal planned,
                                              BigDecimal applied,
                                              BigDecimal uptake,
                                              String unit) {
        NutrientBudgetEntity value = new NutrientBudgetEntity();
        value.id = UUID.randomUUID();
        value.cropCycleId = cycleId;
        value.nutrientCode = code;
        value.plannedQuantity = planned;
        value.appliedQuantity = applied;
        value.estimatedCropUptake = uptake;
        value.estimatedBalance = applied.subtract(
                uptake == null ? BigDecimal.ZERO : uptake);
        value.quantityUnit = unit;
        value.calculatedAt = Instant.now();
        value.calculationVersion = "v1";
        return value;
    }

    public String nutrientCode() { return nutrientCode; }
    public BigDecimal plannedQuantity() { return plannedQuantity; }
    public BigDecimal appliedQuantity() { return appliedQuantity; }
    public BigDecimal estimatedCropUptake() { return estimatedCropUptake; }
    public BigDecimal estimatedBalance() { return estimatedBalance; }
    public String quantityUnit() { return quantityUnit; }
}
