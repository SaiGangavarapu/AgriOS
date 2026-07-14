package com.agrios.platform.knowledge.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "crop_requirement", schema = "knowledge")
public class CropRequirementEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID cropId;
    @Column(nullable = false) private String requirementType;
    private BigDecimal minimumValue;
    private BigDecimal maximumValue;
    private String unitCode;
    @Column(nullable = false) private boolean hardConstraint;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String applicabilityJson;
    private String evidenceReference;
    @Column(nullable = false) private Instant createdAt;

    protected CropRequirementEntity() {}

    public static CropRequirementEntity create(UUID cropId, String type,
                                               BigDecimal min, BigDecimal max,
                                               String unit, boolean hard,
                                               String applicability,
                                               String evidence) {
        CropRequirementEntity value = new CropRequirementEntity();
        value.id = UUID.randomUUID();
        value.cropId = cropId;
        value.requirementType = type;
        value.minimumValue = min;
        value.maximumValue = max;
        value.unitCode = unit;
        value.hardConstraint = hard;
        value.applicabilityJson = applicability;
        value.evidenceReference = evidence;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID cropId() { return cropId; }
    public String requirementType() { return requirementType; }
    public BigDecimal minimumValue() { return minimumValue; }
    public BigDecimal maximumValue() { return maximumValue; }
    public String unitCode() { return unitCode; }
    public boolean hardConstraint() { return hardConstraint; }
    public String applicabilityJson() { return applicabilityJson; }
    public String evidenceReference() { return evidenceReference; }
}
