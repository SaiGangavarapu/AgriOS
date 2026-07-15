package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "crop_health_assessment", schema = "crophealth")
public class CropHealthAssessmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private LocalDate assessmentDate;
    @Column(nullable = false) private String healthStatus;
    private BigDecimal vigorScore;
    private BigDecimal canopyScore;
    private BigDecimal standEstablishmentScore;
    private BigDecimal pestPressureScore;
    private BigDecimal diseasePressureScore;
    private BigDecimal nutrientStressScore;
    private BigDecimal waterStressScore;
    @Column(nullable = false) private BigDecimal compositeScore;
    @Column(nullable = false) private BigDecimal confidenceScore;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceSnapshot;
    @Column(nullable = false) private String assessmentVersion;
    @Column(nullable = false) private Instant assessedAt;

    protected CropHealthAssessmentEntity() {}

    public static CropHealthAssessmentEntity create(
            UUID tenantId, UUID fieldId, UUID cycleId, LocalDate date,
            String status, BigDecimal vigor, BigDecimal canopy,
            BigDecimal establishment, BigDecimal pestPressure,
            BigDecimal diseasePressure, BigDecimal nutrientStress,
            BigDecimal waterStress, BigDecimal composite,
            BigDecimal confidence, String reasons, String evidence) {
        CropHealthAssessmentEntity value = new CropHealthAssessmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.cropCycleId = cycleId;
        value.assessmentDate = date;
        value.healthStatus = status;
        value.vigorScore = vigor;
        value.canopyScore = canopy;
        value.standEstablishmentScore = establishment;
        value.pestPressureScore = pestPressure;
        value.diseasePressureScore = diseasePressure;
        value.nutrientStressScore = nutrientStress;
        value.waterStressScore = waterStress;
        value.compositeScore = composite;
        value.confidenceScore = confidence;
        value.reasonCodes = reasons;
        value.evidenceSnapshot = evidence;
        value.assessmentVersion = "v1";
        value.assessedAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String healthStatus() { return healthStatus; }
    public BigDecimal compositeScore() { return compositeScore; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public String reasonCodes() { return reasonCodes; }
}
