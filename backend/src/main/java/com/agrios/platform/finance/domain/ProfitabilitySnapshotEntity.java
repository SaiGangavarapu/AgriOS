package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.math.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "profitability_snapshot", schema = "finance")
public class ProfitabilitySnapshotEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    private UUID fieldId;
    private UUID cropCycleId;
    @Column(nullable = false) private LocalDate periodStart;
    @Column(nullable = false) private LocalDate periodEnd;
    @Column(nullable = false) private BigDecimal revenue;
    @Column(nullable = false) private BigDecimal operatingCost;
    @Column(nullable = false) private BigDecimal allocatedOverhead;
    @Column(nullable = false) private BigDecimal depreciation;
    @Column(nullable = false) private BigDecimal financeCost;
    @Column(nullable = false) private BigDecimal netProfit;
    private BigDecimal roiPercent;
    private BigDecimal costPerUnit;
    private BigDecimal productionQuantity;
    private String productionUnit;
    @Column(nullable = false) private String calculationVersion;
    @Column(nullable = false) private Instant calculatedAt;

    protected ProfitabilitySnapshotEntity() {}

    public static ProfitabilitySnapshotEntity create(
            UUID tenantId, UUID farmerId, UUID fieldId, UUID cropCycleId,
            LocalDate start, LocalDate end, BigDecimal revenue,
            BigDecimal operatingCost, BigDecimal overhead,
            BigDecimal depreciation, BigDecimal financeCost,
            BigDecimal productionQuantity, String productionUnit) {
        ProfitabilitySnapshotEntity value = new ProfitabilitySnapshotEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.fieldId = fieldId;
        value.cropCycleId = cropCycleId;
        value.periodStart = start;
        value.periodEnd = end;
        value.revenue = revenue;
        value.operatingCost = operatingCost;
        value.allocatedOverhead = overhead;
        value.depreciation = depreciation;
        value.financeCost = financeCost;
        BigDecimal totalCost = operatingCost.add(overhead).add(depreciation).add(financeCost);
        value.netProfit = revenue.subtract(totalCost);
        if (totalCost.signum() > 0) {
            value.roiPercent = value.netProfit.multiply(BigDecimal.valueOf(100))
                    .divide(totalCost, 4, RoundingMode.HALF_UP);
        }
        value.productionQuantity = productionQuantity;
        value.productionUnit = productionUnit;
        if (productionQuantity != null && productionQuantity.signum() > 0) {
            value.costPerUnit = totalCost.divide(productionQuantity, 4, RoundingMode.HALF_UP);
        }
        value.calculationVersion = "v1";
        value.calculatedAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal revenue() { return revenue; }
    public BigDecimal operatingCost() { return operatingCost; }
    public BigDecimal netProfit() { return netProfit; }
    public BigDecimal roiPercent() { return roiPercent; }
    public BigDecimal costPerUnit() { return costPerUnit; }
}
