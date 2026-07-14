package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "irrigation_plan", schema = "irrigation")
public class IrrigationPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID waterSourceId;
    private UUID waterProfileId;
    @Column(nullable = false) private String irrigationMethod;
    @Column(nullable = false) private String status;
    private BigDecimal seasonalWaterRequirementMm;
    private BigDecimal plannedWaterVolumeM3;
    private BigDecimal efficiencyPercent;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String recommendationBasis;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String warnings;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected IrrigationPlanEntity() {}

    public static IrrigationPlanEntity create(
            UUID tenantId, UUID cycleId, UUID sourceId, UUID profileId,
            String method, BigDecimal requirementMm,
            BigDecimal volumeM3, BigDecimal efficiency,
            String basis, String warnings, UUID actorId) {
        IrrigationPlanEntity value = new IrrigationPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cycleId;
        value.waterSourceId = sourceId;
        value.waterProfileId = profileId;
        value.irrigationMethod = method;
        value.status = "GENERATED";
        value.seasonalWaterRequirementMm = requirementMm;
        value.plannedWaterVolumeM3 = volumeM3;
        value.efficiencyPercent = efficiency;
        value.recommendationBasis = basis;
        value.warnings = warnings;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("GENERATED")) {
            throw new IllegalStateException("Only generated plans can be approved.");
        }
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID waterSourceId() { return waterSourceId; }
    public UUID waterProfileId() { return waterProfileId; }
    public String irrigationMethod() { return irrigationMethod; }
    public String status() { return status; }
    public BigDecimal seasonalWaterRequirementMm() { return seasonalWaterRequirementMm; }
    public BigDecimal plannedWaterVolumeM3() { return plannedWaterVolumeM3; }
    public BigDecimal efficiencyPercent() { return efficiencyPercent; }
    public String recommendationBasis() { return recommendationBasis; }
    public String warnings() { return warnings; }
    public long version() { return version; }
}
