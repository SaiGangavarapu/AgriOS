package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assistant_message", schema = "ai")
public class AssistantMessageEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID assistantSessionId;
    @Column(nullable = false) private String messageRole;
    @Column(nullable = false, columnDefinition = "text") private String messageText;
    private String modelCode;
    private Integer promptTokens;
    private Integer completionTokens;
    private Long latencyMs;
    private String finishReason;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;
    @Column(nullable = false) private Instant createdAt;

    protected AssistantMessageEntity() {}

    public static AssistantMessageEntity create(
            UUID sessionId, String role, String text,
            String modelCode, String metadata) {
        AssistantMessageEntity value = new AssistantMessageEntity();
        value.id = UUID.randomUUID();
        value.assistantSessionId = sessionId;
        value.messageRole = role;
        value.messageText = text;
        value.modelCode = modelCode;
        value.metadata = metadata;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String messageRole() { return messageRole; }
    public String messageText() { return messageText; }
}
