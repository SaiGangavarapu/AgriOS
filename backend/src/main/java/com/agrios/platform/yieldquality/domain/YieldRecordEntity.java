package com.agrios.platform.yieldquality.domain;

import jakarta.persistence.*;
import java.math.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "yield_record", schema = "yieldquality")
public class YieldRecordEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false, unique = true) private UUID harvestBatchId;
    @Column(nullable = false) private BigDecimal harvestedQuantity;
    @Column(nullable = false) private String harvestedUnit;
    @Column(nullable = false) private BigDecimal fieldAreaHectares;
    @Column(nullable = false) private BigDecimal yieldPerHectare;
    private BigDecimal expectedYieldPerHectare;
    private BigDecimal deviationPercent;
    private BigDecimal marketableQuantity;
    private BigDecimal rejectedQuantity;
    @Column(nullable = false) private Instant recordedAt;
    @Column(nullable = false) private String calculationVersion;

    protected YieldRecordEntity() {}

    public static YieldRecordEntity create(
            UUID tenantId, UUID cycleId, UUID fieldId, UUID batchId,
            BigDecimal harvested, String unit, BigDecimal area,
            BigDecimal expectedYieldPerHectare,
            BigDecimal marketable, BigDecimal rejected) {
        YieldRecordEntity value = new YieldRecordEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cycleId;
        value.fieldId = fieldId;
        value.harvestBatchId = batchId;
        value.harvestedQuantity = harvested;
        value.harvestedUnit = unit;
        value.fieldAreaHectares = area;
        value.yieldPerHectare = harvested.divide(area, 4, RoundingMode.HALF_UP);
        value.expectedYieldPerHectare = expectedYieldPerHectare;
        if (expectedYieldPerHectare != null && expectedYieldPerHectare.signum() != 0) {
            value.deviationPercent = value.yieldPerHectare
                    .subtract(expectedYieldPerHectare)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(expectedYieldPerHectare, 4, RoundingMode.HALF_UP);
        }
        value.marketableQuantity = marketable;
        value.rejectedQuantity = rejected;
        value.recordedAt = Instant.now();
        value.calculationVersion = "v1";
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal yieldPerHectare() { return yieldPerHectare; }
    public BigDecimal deviationPercent() { return deviationPercent; }
}
