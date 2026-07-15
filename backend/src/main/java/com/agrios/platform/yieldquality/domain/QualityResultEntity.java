package com.agrios.platform.yieldquality.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quality_result", schema = "yieldquality")
public class QualityResultEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID qualityAssessmentId;
    @Column(nullable = false) private UUID qualityParameterId;
    private BigDecimal numericValue;
    private String textValue;
    private Boolean booleanValue;
    private Boolean withinSpecification;
    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false) private Instant createdAt;

    protected QualityResultEntity() {}

    public static QualityResultEntity create(
            UUID assessmentId, UUID parameterId, BigDecimal numericValue,
            String textValue, Boolean booleanValue,
            Boolean withinSpecification, String notes) {
        QualityResultEntity value = new QualityResultEntity();
        value.id = UUID.randomUUID();
        value.qualityAssessmentId = assessmentId;
        value.qualityParameterId = parameterId;
        value.numericValue = numericValue;
        value.textValue = textValue;
        value.booleanValue = booleanValue;
        value.withinSpecification = withinSpecification;
        value.notes = notes;
        value.createdAt = Instant.now();
        return value;
    }

    public Boolean withinSpecification() { return withinSpecification; }
}
