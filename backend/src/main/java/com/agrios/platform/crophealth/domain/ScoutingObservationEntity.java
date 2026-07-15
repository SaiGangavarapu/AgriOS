package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "scouting_observation", schema = "crophealth")
public class ScoutingObservationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID scoutingVisitId;
    @Column(nullable = false) private String observationType;
    private UUID pestId;
    private UUID diseaseId;
    private String observedSymptom;
    private Integer affectedPlantCount;
    private BigDecimal incidencePercent;
    private BigDecimal severityPercent;
    private BigDecimal populationCount;
    private String populationUnit;
    private String cropStageCode;
    private BigDecimal confidenceScore;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceJson;
    @Column(nullable = false) private Instant createdAt;

    protected ScoutingObservationEntity() {}

    public static ScoutingObservationEntity create(
            UUID visitId, String type, UUID pestId, UUID diseaseId,
            String symptom, Integer affectedPlantCount,
            BigDecimal incidence, BigDecimal severity,
            BigDecimal population, String populationUnit,
            String cropStageCode, BigDecimal confidence,
            String evidenceJson) {
        ScoutingObservationEntity value = new ScoutingObservationEntity();
        value.id = UUID.randomUUID();
        value.scoutingVisitId = visitId;
        value.observationType = type;
        value.pestId = pestId;
        value.diseaseId = diseaseId;
        value.observedSymptom = symptom;
        value.affectedPlantCount = affectedPlantCount;
        value.incidencePercent = incidence;
        value.severityPercent = severity;
        value.populationCount = population;
        value.populationUnit = populationUnit;
        value.cropStageCode = cropStageCode;
        value.confidenceScore = confidence;
        value.evidenceJson = evidenceJson;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String observationType() { return observationType; }
    public UUID pestId() { return pestId; }
    public UUID diseaseId() { return diseaseId; }
    public BigDecimal incidencePercent() { return incidencePercent; }
    public BigDecimal severityPercent() { return severityPercent; }
}
