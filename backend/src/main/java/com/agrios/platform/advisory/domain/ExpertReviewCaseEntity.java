package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "expert_review_case", schema = "advisory")
public class ExpertReviewCaseEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String caseType;
    @Column(nullable = false) private String subjectType;
    @Column(nullable = false) private UUID subjectId;
    private UUID farmerId;
    private UUID fieldId;
    private UUID cropCycleId;
    private UUID requestedBy;
    @Column(nullable = false) private Instant requestedAt;
    @Column(nullable = false) private String priority;
    @Column(nullable = false) private String status;
    private Instant dueAt;
    @Column(nullable = false, columnDefinition = "text") private String question;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String contextSnapshot;
    private UUID assignedExpertId;
    private Instant assignedAt;
    private Instant completedAt;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected ExpertReviewCaseEntity() {}

    public static ExpertReviewCaseEntity create(
            UUID tenantId, String caseType, String subjectType,
            UUID subjectId, UUID farmerId, UUID fieldId,
            UUID cropCycleId, UUID requestedBy, String priority,
            Instant dueAt, String question, String contextSnapshot) {
        ExpertReviewCaseEntity value = new ExpertReviewCaseEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.caseType = caseType;
        value.subjectType = subjectType;
        value.subjectId = subjectId;
        value.farmerId = farmerId;
        value.fieldId = fieldId;
        value.cropCycleId = cropCycleId;
        value.requestedBy = requestedBy;
        value.requestedAt = Instant.now();
        value.priority = priority;
        value.status = "OPEN";
        value.dueAt = dueAt;
        value.question = question;
        value.contextSnapshot = contextSnapshot;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void assign(UUID expertId) {
        if (!status.equals("OPEN")) throw new IllegalStateException("Case is not open.");
        assignedExpertId = expertId;
        assignedAt = Instant.now();
        status = "ASSIGNED";
        updatedAt = Instant.now();
    }

    public void startReview() {
        if (!status.equals("ASSIGNED")) throw new IllegalStateException("Case is not assigned.");
        status = "IN_REVIEW";
        updatedAt = Instant.now();
    }

    public void requestMoreInformation() {
        if (!status.equals("IN_REVIEW")) throw new IllegalStateException("Case is not in review.");
        status = "MORE_INFORMATION_REQUIRED";
        updatedAt = Instant.now();
    }

    public void complete() {
        if (!status.equals("IN_REVIEW") && !status.equals("MORE_INFORMATION_REQUIRED")) {
            throw new IllegalStateException("Case cannot be completed.");
        }
        status = "COMPLETED";
        completedAt = Instant.now();
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public UUID fieldId() { return fieldId; }
    public UUID cropCycleId() { return cropCycleId; }
    public String caseType() { return caseType; }
    public String subjectType() { return subjectType; }
    public UUID subjectId() { return subjectId; }
    public String priority() { return priority; }
    public String status() { return status; }
    public String question() { return question; }
    public UUID assignedExpertId() { return assignedExpertId; }
    public long version() { return version; }
}
