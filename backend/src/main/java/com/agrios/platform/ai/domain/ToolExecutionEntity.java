package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tool_execution", schema = "ai")
public class ToolExecutionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID assistantSessionId;
    private UUID assistantMessageId;
    @Column(nullable = false) private UUID toolDefinitionId;
    @Column(nullable = false) private String idempotencyKey;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String inputPayload;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String outputPayload;
    @Column(nullable = false) private String status;
    private String errorCode;
    @Column(columnDefinition = "text") private String errorMessage;
    @Column(nullable = false) private Instant requestedAt;
    private Instant completedAt;

    protected ToolExecutionEntity() {}

    public static ToolExecutionEntity create(
            UUID tenantId, UUID sessionId, UUID messageId,
            UUID toolDefinitionId, String idempotencyKey,
            String inputPayload, boolean requiresConfirmation) {
        ToolExecutionEntity value = new ToolExecutionEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.assistantSessionId = sessionId;
        value.assistantMessageId = messageId;
        value.toolDefinitionId = toolDefinitionId;
        value.idempotencyKey = idempotencyKey;
        value.inputPayload = inputPayload;
        value.outputPayload = "{}";
        value.status = requiresConfirmation ? "AWAITING_CONFIRMATION" : "APPROVED";
        value.requestedAt = Instant.now();
        return value;
    }

    public void approve() {
        if (!status.equals("AWAITING_CONFIRMATION")) {
            throw new IllegalStateException("Tool execution is not awaiting confirmation.");
        }
        status = "APPROVED";
    }

    public void succeed(String outputPayload) {
        status = "SUCCEEDED";
        this.outputPayload = outputPayload;
        completedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String status() { return status; }
}
