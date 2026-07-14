package com.agrios.platform.nutrient.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "nutrient_plan", schema = "nutrient")
public class NutrientPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID soilProfileId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FarmingApproach farmingApproach;
    private BigDecimal targetYield;
    private String targetYieldUnit;
    @Column(nullable = false) private String status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String recommendationBasis;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String warnings;
    @Column(columnDefinition = "text") private String approvalNotes;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected NutrientPlanEntity() {}

    public static NutrientPlanEntity create(UUID tenantId, UUID cropCycleId,
                                            UUID soilProfileId,
                                            FarmingApproach approach,
                                            BigDecimal targetYield,
                                            String targetYieldUnit,
                                            String basis, String warnings,
                                            UUID actorId) {
        NutrientPlanEntity value = new NutrientPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cropCycleId;
        value.soilProfileId = soilProfileId;
        value.farmingApproach = approach;
        value.targetYield = targetYield;
        value.targetYieldUnit = targetYieldUnit;
        value.status = "GENERATED";
        value.recommendationBasis = basis;
        value.warnings = warnings;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void approve(String notes, UUID actorId) {
        if (!status.equals("GENERATED") && !status.equals("FARMER_REVIEWED") &&
                !status.equals("EXPERT_REVIEW")) {
            throw new IllegalStateException("Plan cannot be approved from status " + status);
        }
        status = "APPROVED";
        approvalNotes = notes;
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID soilProfileId() { return soilProfileId; }
    public FarmingApproach farmingApproach() { return farmingApproach; }
    public BigDecimal targetYield() { return targetYield; }
    public String targetYieldUnit() { return targetYieldUnit; }
    public String status() { return status; }
    public String recommendationBasis() { return recommendationBasis; }
    public String warnings() { return warnings; }
    public String approvalNotes() { return approvalNotes; }
    public Instant approvedAt() { return approvedAt; }
    public UUID approvedBy() { return approvedBy; }
    public long version() { return version; }
}
