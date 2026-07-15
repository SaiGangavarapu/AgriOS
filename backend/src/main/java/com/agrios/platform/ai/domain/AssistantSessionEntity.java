package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assistant_session", schema = "ai")
public class AssistantSessionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID farmerId;
    @Column(nullable = false) private String assistantType;
    private String title;
    @Column(nullable = false) private String languageCode;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected AssistantSessionEntity() {}

    public static AssistantSessionEntity create(
            UUID tenantId, UUID farmerId, String assistantType,
            String title, String languageCode) {
        AssistantSessionEntity value = new AssistantSessionEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.assistantType = assistantType;
        value.title = title;
        value.languageCode = languageCode;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public String assistantType() { return assistantType; }
    public String languageCode() { return languageCode; }
    public String status() { return status; }
}
