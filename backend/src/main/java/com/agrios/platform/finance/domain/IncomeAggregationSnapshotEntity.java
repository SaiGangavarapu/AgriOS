package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "income_aggregation_snapshot", schema = "finance")
public class IncomeAggregationSnapshotEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    @Column(nullable = false) private LocalDate periodStart;
    @Column(nullable = false) private LocalDate periodEnd;
    @Column(nullable = false) private BigDecimal cropIncome;
    @Column(nullable = false) private BigDecimal livestockIncome;
    @Column(nullable = false) private BigDecimal dairyIncome;
    @Column(nullable = false) private BigDecimal marketplaceIncome;
    @Column(nullable = false) private BigDecimal sellerErpIncome;
    @Column(nullable = false) private BigDecimal subsidyIncome;
    @Column(nullable = false) private BigDecimal insuranceIncome;
    @Column(nullable = false) private BigDecimal otherIncome;
    @Column(nullable = false) private BigDecimal totalIncome;
    @Column(nullable = false) private Instant calculatedAt;
    @Column(nullable = false) private String calculationVersion;

    protected IncomeAggregationSnapshotEntity() {}

    public static IncomeAggregationSnapshotEntity create(
            UUID tenantId, UUID farmerId, LocalDate start, LocalDate end,
            BigDecimal crop, BigDecimal livestock, BigDecimal dairy,
            BigDecimal marketplace, BigDecimal seller,
            BigDecimal subsidy, BigDecimal insurance, BigDecimal other) {
        IncomeAggregationSnapshotEntity value = new IncomeAggregationSnapshotEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.periodStart = start;
        value.periodEnd = end;
        value.cropIncome = crop;
        value.livestockIncome = livestock;
        value.dairyIncome = dairy;
        value.marketplaceIncome = marketplace;
        value.sellerErpIncome = seller;
        value.subsidyIncome = subsidy;
        value.insuranceIncome = insurance;
        value.otherIncome = other;
        value.totalIncome = crop.add(livestock).add(dairy).add(marketplace)
                .add(seller).add(subsidy).add(insurance).add(other);
        value.calculatedAt = Instant.now();
        value.calculationVersion = "v1";
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal totalIncome() { return totalIncome; }
}
