package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "ipm_plan", schema = "crophealth")
public class IpmPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private UUID fieldId;
    private UUID pestId;
    private UUID diseaseId;
    @Column(nullable = false) private String planType;
    @Column(nullable = false) private String status;
    @Column(nullable = false, columnDefinition = "text") private String strategySummary;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String preventionMeasures;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String monitoringMeasures;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String nonChemicalMeasures;
    @Column(nullable = false) private boolean chemicalLastResort;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String warnings;
    @Column(columnDefinition = "text") private String approvalNotes;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected IpmPlanEntity() {}

    public static IpmPlanEntity create(
            UUID tenantId, UUID cycleId, UUID fieldId,
            UUID pestId, UUID diseaseId, String planType,
            String strategy, String prevention, String monitoring,
            String nonChemical, boolean chemicalLastResort,
            String warnings) {
        IpmPlanEntity value = new IpmPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cycleId;
        value.fieldId = fieldId;
        value.pestId = pestId;
        value.diseaseId = diseaseId;
        value.planType = planType;
        value.status = "GENERATED";
        value.strategySummary = strategy;
        value.preventionMeasures = prevention;
        value.monitoringMeasures = monitoring;
        value.nonChemicalMeasures = nonChemical;
        value.chemicalLastResort = chemicalLastResort;
        value.warnings = warnings;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void approve(UUID actorId, String notes) {
        if (!status.equals("GENERATED") && !status.equals("EXPERT_REVIEW")) {
            throw new IllegalStateException("IPM plan cannot be approved.");
        }
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        approvalNotes = notes;
        updatedAt = approvedAt;
    }

    public UUID id() { return id; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID fieldId() { return fieldId; }
    public UUID pestId() { return pestId; }
    public UUID diseaseId() { return diseaseId; }
    public String planType() { return planType; }
    public String status() { return status; }
    public String strategySummary() { return strategySummary; }
    public String preventionMeasures() { return preventionMeasures; }
    public String monitoringMeasures() { return monitoringMeasures; }
    public String nonChemicalMeasures() { return nonChemicalMeasures; }
    public String warnings() { return warnings; }
    public long version() { return version; }
}
