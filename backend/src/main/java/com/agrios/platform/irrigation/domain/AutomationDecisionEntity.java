package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "automation_decision", schema = "irrigation")
public class AutomationDecisionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID automationPolicyId;
    private UUID irrigationScheduleId;
    @Column(nullable = false) private Instant decidedAt;
    @Column(nullable = false) private String decisionType;
    @Column(nullable = false) private String decisionStatus;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String inputSnapshot;
    @Column(nullable = false) private BigDecimal confidenceScore;
    @Column(nullable = false) private String requestedBy;
    private UUID approvedBy;
    private Instant approvedAt;
    private Instant executedAt;
    @Column(nullable = false) private Instant createdAt;

    protected AutomationDecisionEntity() {}

    public static AutomationDecisionEntity create(
            UUID tenantId, UUID policyId, UUID scheduleId,
            String decisionType, String status,
            String reasons, String snapshot,
            BigDecimal confidence) {
        AutomationDecisionEntity value = new AutomationDecisionEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.automationPolicyId = policyId;
        value.irrigationScheduleId = scheduleId;
        value.decidedAt = Instant.now();
        value.decisionType = decisionType;
        value.decisionStatus = status;
        value.reasonCodes = reasons;
        value.inputSnapshot = snapshot;
        value.confidenceScore = confidence;
        value.requestedBy = "RULE_ENGINE";
        value.createdAt = Instant.now();
        return value;
    }

    public void approve(UUID actorId) {
        if (!decisionStatus.equals("PENDING")) {
            throw new IllegalStateException("Decision is not pending.");
        }
        decisionStatus = "APPROVED";
        approvedBy = actorId;
        approvedAt = Instant.now();
    }

    public void execute() {
        decisionStatus = "EXECUTED";
        executedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID automationPolicyId() { return automationPolicyId; }
    public String decisionType() { return decisionType; }
    public String decisionStatus() { return decisionStatus; }
}
