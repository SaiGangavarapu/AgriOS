package com.agrios.platform.yieldquality.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quality_assessment", schema = "yieldquality")
public class QualityAssessmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID harvestBatchId;
    @Column(nullable = false) private Instant assessedAt;
    @Column(nullable = false) private String assessorType;
    private UUID assessorId;
    @Column(nullable = false) private String status;
    private String assignedGrade;
    @Column(nullable = false) private String marketabilityStatus;
    @Column(columnDefinition = "text") private String notes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected QualityAssessmentEntity() {}

    public static QualityAssessmentEntity create(
            UUID tenantId, UUID batchId, Instant assessedAt,
            String assessorType, UUID assessorId, String notes) {
        QualityAssessmentEntity value = new QualityAssessmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.harvestBatchId = batchId;
        value.assessedAt = assessedAt;
        value.assessorType = assessorType;
        value.assessorId = assessorId;
        value.status = "IN_PROGRESS";
        value.marketabilityStatus = "PENDING";
        value.notes = notes;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void complete(String grade, String marketability) {
        status = "COMPLETED";
        assignedGrade = grade;
        marketabilityStatus = marketability;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID harvestBatchId() { return harvestBatchId; }
    public String status() { return status; }
    public String assignedGrade() { return assignedGrade; }
    public String marketabilityStatus() { return marketabilityStatus; }
    public long version() { return version; }
}
