package com.agrios.platform.tasks.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "task_event", schema = "tasks")
public class TaskEventEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID taskId;
    @Column(nullable = false) private String eventType;
    @Column(nullable = false) private Instant occurredAt;
    private UUID actorId;
    @Column(columnDefinition = "text") private String reason;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;

    protected TaskEventEntity() {}

    public static TaskEventEntity create(UUID taskId, String type,
                                         UUID actorId, String reason,
                                         String metadata) {
        TaskEventEntity value = new TaskEventEntity();
        value.id = UUID.randomUUID();
        value.taskId = taskId;
        value.eventType = type;
        value.occurredAt = Instant.now();
        value.actorId = actorId;
        value.reason = reason;
        value.metadata = metadata == null ? "{}" : metadata;
        return value;
    }
}
