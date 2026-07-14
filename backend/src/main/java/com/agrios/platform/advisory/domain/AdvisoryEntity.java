package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "advisory", schema = "advisory")
public class AdvisoryEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID farmerId;
    private UUID farmId;
    private UUID fieldId;
    private UUID cropCycleId;
    @Column(nullable = false) private String advisoryType;
    @Column(nullable = false) private String priority;
    @Column(nullable = false) private String title;
    @Column(nullable = false, columnDefinition = "text") private String summary;
    @Column(columnDefinition = "text") private String detailedGuidance;
    @Column(nullable = false) private String sourceType;
    private UUID sourceReferenceId;
    @Column(nullable = false) private String language;
    @Column(nullable = false) private Instant validFrom;
    private Instant validUntil;
    @Column(nullable = false) private String status;
    private BigDecimal confidenceScore;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceSnapshot;
    private Instant approvedAt;
    private UUID approvedBy;
    private Instant publishedAt;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected AdvisoryEntity() {}

    public static AdvisoryEntity create(
            UUID tenantId, UUID farmerId, UUID farmId, UUID fieldId,
            UUID cropCycleId, String advisoryType, String priority,
            String title, String summary, String detailedGuidance,
            String sourceType, UUID sourceReferenceId, String language,
            Instant validFrom, Instant validUntil, BigDecimal confidence,
            String reasonCodes, String evidenceSnapshot, UUID actorId) {
        AdvisoryEntity value = new AdvisoryEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.farmId = farmId;
        value.fieldId = fieldId;
        value.cropCycleId = cropCycleId;
        value.advisoryType = advisoryType;
        value.priority = priority;
        value.title = title;
        value.summary = summary;
        value.detailedGuidance = detailedGuidance;
        value.sourceType = sourceType;
        value.sourceReferenceId = sourceReferenceId;
        value.language = language;
        value.validFrom = validFrom;
        value.validUntil = validUntil;
        value.status = "DRAFT";
        value.confidenceScore = confidence;
        value.reasonCodes = reasonCodes;
        value.evidenceSnapshot = evidenceSnapshot;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void submitForReview(UUID actorId) {
        if (!status.equals("DRAFT")) throw new IllegalStateException("Only draft advisory can be submitted.");
        status = "PENDING_REVIEW";
        touch(actorId);
    }

    public void approve(UUID actorId) {
        if (!status.equals("PENDING_REVIEW") && !status.equals("DRAFT")) {
            throw new IllegalStateException("Advisory cannot be approved.");
        }
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        touch(actorId);
    }

    public void publish(UUID actorId) {
        if (!status.equals("APPROVED")) throw new IllegalStateException("Only approved advisory can be published.");
        status = "PUBLISHED";
        publishedAt = Instant.now();
        touch(actorId);
    }

    public void withdraw(UUID actorId) {
        if (!status.equals("PUBLISHED") && !status.equals("APPROVED")) {
            throw new IllegalStateException("Advisory cannot be withdrawn.");
        }
        status = "WITHDRAWN";
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public UUID farmId() { return farmId; }
    public UUID fieldId() { return fieldId; }
    public UUID cropCycleId() { return cropCycleId; }
    public String advisoryType() { return advisoryType; }
    public String priority() { return priority; }
    public String title() { return title; }
    public String summary() { return summary; }
    public String detailedGuidance() { return detailedGuidance; }
    public String sourceType() { return sourceType; }
    public String language() { return language; }
    public Instant validFrom() { return validFrom; }
    public Instant validUntil() { return validUntil; }
    public String status() { return status; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public String reasonCodes() { return reasonCodes; }
    public String evidenceSnapshot() { return evidenceSnapshot; }
    public long version() { return version; }
}
