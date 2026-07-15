package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "ai_model_profile", schema = "ai")
public class AiModelProfileEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID providerId;
    @Column(nullable = false) private String modelCode;
    @Column(nullable = false) private String modelName;
    @Column(nullable = false) private String modelRole;
    private Integer maxInputTokens;
    private Integer maxOutputTokens;
    private BigDecimal temperature;
    @Column(nullable = false) private boolean enabled;
    @Column(nullable = false) private boolean defaultForRole;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String configurationJson;
    @Column(nullable = false) private Instant createdAt;

    protected AiModelProfileEntity() {}

    public static AiModelProfileEntity create(
            UUID tenantId, UUID providerId, String modelCode,
            String modelName, String modelRole, Integer maxInputTokens,
            Integer maxOutputTokens, BigDecimal temperature,
            boolean defaultForRole, String configurationJson) {
        AiModelProfileEntity value = new AiModelProfileEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.providerId = providerId;
        value.modelCode = modelCode;
        value.modelName = modelName;
        value.modelRole = modelRole;
        value.maxInputTokens = maxInputTokens;
        value.maxOutputTokens = maxOutputTokens;
        value.temperature = temperature;
        value.enabled = true;
        value.defaultForRole = defaultForRole;
        value.configurationJson = configurationJson;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String modelCode() { return modelCode; }
    public String modelRole() { return modelRole; }
}
