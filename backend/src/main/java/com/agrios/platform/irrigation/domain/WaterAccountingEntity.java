package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "water_accounting", schema = "irrigation")
public class WaterAccountingEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID waterSourceId;
    @Column(nullable = false) private BigDecimal rainfallEffectiveMm;
    @Column(nullable = false) private BigDecimal irrigationAppliedMm;
    @Column(nullable = false) private BigDecimal irrigationVolumeM3;
    private BigDecimal estimatedCropDemandMm;
    private BigDecimal estimatedDeficitMm;
    @Column(nullable = false) private Instant calculatedAt;
    @Column(nullable = false) private String calculationVersion;

    protected WaterAccountingEntity() {}

    public static WaterAccountingEntity create(
            UUID cycleId, UUID sourceId, BigDecimal rainfall,
            BigDecimal irrigationMm, BigDecimal volume,
            BigDecimal demand) {
        WaterAccountingEntity value = new WaterAccountingEntity();
        value.id = UUID.randomUUID();
        value.cropCycleId = cycleId;
        value.waterSourceId = sourceId;
        value.rainfallEffectiveMm = rainfall;
        value.irrigationAppliedMm = irrigationMm;
        value.irrigationVolumeM3 = volume;
        value.estimatedCropDemandMm = demand;
        value.estimatedDeficitMm = demand == null ? null :
                demand.subtract(rainfall).subtract(irrigationMm);
        value.calculatedAt = Instant.now();
        value.calculationVersion = "v1";
        return value;
    }

    public BigDecimal rainfallEffectiveMm() { return rainfallEffectiveMm; }
    public BigDecimal irrigationAppliedMm() { return irrigationAppliedMm; }
    public BigDecimal irrigationVolumeM3() { return irrigationVolumeM3; }
    public BigDecimal estimatedCropDemandMm() { return estimatedCropDemandMm; }
    public BigDecimal estimatedDeficitMm() { return estimatedDeficitMm; }
}
