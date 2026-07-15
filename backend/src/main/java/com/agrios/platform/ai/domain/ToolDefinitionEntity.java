package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tool_definition", schema = "ai")
public class ToolDefinitionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String toolCode;
    @Column(nullable = false) private String toolName;
    @Column(nullable = false) private String domainModule;
    @Column(nullable = false, columnDefinition = "text") private String description;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String inputSchema;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String outputSchema;
    @Column(nullable = false) private String riskLevel;
    @Column(nullable = false) private boolean requiresConfirmation;
    @Column(nullable = false) private boolean enabled;
    @Column(nullable = false) private Instant createdAt;

    protected ToolDefinitionEntity() {}

    public static ToolDefinitionEntity create(
            UUID tenantId, String toolCode, String toolName,
            String domainModule, String description,
            String inputSchema, String outputSchema,
            String riskLevel, boolean requiresConfirmation) {
        ToolDefinitionEntity value = new ToolDefinitionEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.toolCode = toolCode;
        value.toolName = toolName;
        value.domainModule = domainModule;
        value.description = description;
        value.inputSchema = inputSchema;
        value.outputSchema = outputSchema;
        value.riskLevel = riskLevel;
        value.requiresConfirmation = requiresConfirmation;
        value.enabled = true;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String toolCode() { return toolCode; }
    public String riskLevel() { return riskLevel; }
    public boolean requiresConfirmation() { return requiresConfirmation; }
    public boolean enabled() { return enabled; }
}
