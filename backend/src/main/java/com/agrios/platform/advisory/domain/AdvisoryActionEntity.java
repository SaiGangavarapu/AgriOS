package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "advisory_action", schema = "advisory")
public class AdvisoryActionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID advisoryId;
    @Column(nullable = false) private String actionCode;
    @Column(nullable = false) private String actionLabel;
    @Column(columnDefinition = "text") private String actionDescription;
    private Instant dueAt;
    @Column(nullable = false) private String actionPriority;
    private String taskTemplateType;
    @Column(nullable = false) private String status;
    private String completionReferenceType;
    private UUID completionReferenceId;
    private Instant completedAt;
    @Column(nullable = false) private Instant createdAt;

    protected AdvisoryActionEntity() {}

    public static AdvisoryActionEntity create(
            UUID advisoryId, String actionCode, String label,
            String description, Instant dueAt, String priority,
            String taskTemplateType) {
        AdvisoryActionEntity value = new AdvisoryActionEntity();
        value.id = UUID.randomUUID();
        value.advisoryId = advisoryId;
        value.actionCode = actionCode;
        value.actionLabel = label;
        value.actionDescription = description;
        value.dueAt = dueAt;
        value.actionPriority = priority;
        value.taskTemplateType = taskTemplateType;
        value.status = "OPEN";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
