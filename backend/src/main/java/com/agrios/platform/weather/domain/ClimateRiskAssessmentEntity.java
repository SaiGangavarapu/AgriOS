package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "climate_risk_assessment", schema = "weather")
public class ClimateRiskAssessmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    private UUID cropCycleId;
    @Column(nullable = false) private LocalDate assessmentWindowStart;
    @Column(nullable = false) private LocalDate assessmentWindowEnd;
    @Column(nullable = false) private String droughtRisk;
    @Column(nullable = false) private String floodRisk;
    @Column(nullable = false) private String heatRisk;
    @Column(nullable = false) private String coldRisk;
    @Column(nullable = false) private String windRisk;
    @Column(nullable = false) private String excessRainRisk;
    @Column(nullable = false) private BigDecimal compositeScore;
    @Column(nullable = false) private BigDecimal confidenceScore;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String sourceVersions;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @Column(nullable = false) private Instant assessedAt;
    @Column(nullable = false) private String assessmentVersion;

    protected ClimateRiskAssessmentEntity() {}

    public static ClimateRiskAssessmentEntity create(
            UUID tenantId, UUID fieldId, UUID cycleId,
            LocalDate start, LocalDate end,
            String drought, String flood, String heat,
            String cold, String wind, String excessRain,
            BigDecimal composite, BigDecimal confidence,
            String sources, String reasons) {
        ClimateRiskAssessmentEntity value = new ClimateRiskAssessmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.cropCycleId = cycleId;
        value.assessmentWindowStart = start;
        value.assessmentWindowEnd = end;
        value.droughtRisk = drought;
        value.floodRisk = flood;
        value.heatRisk = heat;
        value.coldRisk = cold;
        value.windRisk = wind;
        value.excessRainRisk = excessRain;
        value.compositeScore = composite;
        value.confidenceScore = confidence;
        value.sourceVersions = sources;
        value.reasonCodes = reasons;
        value.assessedAt = Instant.now();
        value.assessmentVersion = "v1";
        return value;
    }

    public UUID id() { return id; }
    public String droughtRisk() { return droughtRisk; }
    public String floodRisk() { return floodRisk; }
    public String heatRisk() { return heatRisk; }
    public String coldRisk() { return coldRisk; }
    public String windRisk() { return windRisk; }
    public String excessRainRisk() { return excessRainRisk; }
    public BigDecimal compositeScore() { return compositeScore; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public String reasonCodes() { return reasonCodes; }
}
