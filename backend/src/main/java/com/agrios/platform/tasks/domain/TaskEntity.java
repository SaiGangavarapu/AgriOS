package com.agrios.platform.tasks.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task", schema = "tasks")
public class TaskEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String ownerContext;
    @Column(nullable = false) private UUID ownerEntityId;
    private UUID cropCycleId;
    @Column(nullable = false) private String taskType;
    @Column(nullable = false) private String title;
    @Column(columnDefinition = "text") private String description;
    @Column(nullable = false) private String priority;
    private Instant dueAt;
    private UUID assignedTo;
    @Column(nullable = false) private String status;
    private Instant completedAt;
    private String completionReferenceType;
    private UUID completionReferenceId;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected TaskEntity() {}

    public static TaskEntity create(UUID tenantId, String ownerContext,
                                    UUID ownerEntityId, UUID cropCycleId,
                                    String taskType, String title,
                                    String description, String priority,
                                    Instant dueAt, UUID assignedTo,
                                    UUID actorId) {
        TaskEntity value = new TaskEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.ownerContext = ownerContext;
        value.ownerEntityId = ownerEntityId;
        value.cropCycleId = cropCycleId;
        value.taskType = taskType;
        value.title = title;
        value.description = description;
        value.priority = priority;
        value.dueAt = dueAt;
        value.assignedTo = assignedTo;
        value.status = assignedTo == null ? "OPEN" : "ASSIGNED";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void start(UUID actorId) {
        if (!status.equals("OPEN") && !status.equals("ASSIGNED") && !status.equals("DEFERRED")) {
            throw new IllegalStateException("Task cannot be started.");
        }
        status = "IN_PROGRESS";
        touch(actorId);
    }

    public void defer(Instant dueAt, UUID actorId) {
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            throw new IllegalStateException("Terminal task cannot be deferred.");
        }
        status = "DEFERRED";
        this.dueAt = dueAt;
        touch(actorId);
    }

    public void complete(String referenceType, UUID referenceId, UUID actorId) {
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            throw new IllegalStateException("Task is already terminal.");
        }
        status = "COMPLETED";
        completedAt = Instant.now();
        completionReferenceType = referenceType;
        completionReferenceId = referenceId;
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public String ownerContext() { return ownerContext; }
    public UUID ownerEntityId() { return ownerEntityId; }
    public UUID cropCycleId() { return cropCycleId; }
    public String taskType() { return taskType; }
    public String title() { return title; }
    public String description() { return description; }
    public String priority() { return priority; }
    public Instant dueAt() { return dueAt; }
    public UUID assignedTo() { return assignedTo; }
    public String status() { return status; }
    public Instant completedAt() { return completedAt; }
    public String completionReferenceType() { return completionReferenceType; }
    public UUID completionReferenceId() { return completionReferenceId; }
    public long version() { return version; }
}
