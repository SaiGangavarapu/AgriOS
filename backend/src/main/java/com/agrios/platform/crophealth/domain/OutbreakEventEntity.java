package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbreak_event", schema = "crophealth")
public class OutbreakEventEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID pestId;
    private UUID diseaseId;
    @Column(nullable = false) private String outbreakType;
    @Column(nullable = false) private Instant detectedAt;
    @Column(nullable = false) private String severity;
    private BigDecimal affectedAreaHectares;
    private BigDecimal incidencePercent;
    @Column(nullable = false) private String status;
    private UUID sourceObservationId;
    @Column(columnDefinition = "text") private String containmentNotes;
    private Instant resolvedAt;
    @Column(nullable = false) private Instant createdAt;

    protected OutbreakEventEntity() {}

    public static OutbreakEventEntity create(
            UUID tenantId, UUID fieldId, UUID cycleId,
            UUID pestId, UUID diseaseId, String type,
            Instant detectedAt, String severity,
            BigDecimal affectedArea, BigDecimal incidence,
            UUID sourceObservationId) {
        OutbreakEventEntity value = new OutbreakEventEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.cropCycleId = cycleId;
        value.pestId = pestId;
        value.diseaseId = diseaseId;
        value.outbreakType = type;
        value.detectedAt = detectedAt;
        value.severity = severity;
        value.affectedAreaHectares = affectedArea;
        value.incidencePercent = incidence;
        value.status = "OPEN";
        value.sourceObservationId = sourceObservationId;
        value.createdAt = Instant.now();
        return value;
    }

    public void resolve(String notes) {
        status = "RESOLVED";
        containmentNotes = notes;
        resolvedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String severity() { return severity; }
    public String status() { return status; }
}
