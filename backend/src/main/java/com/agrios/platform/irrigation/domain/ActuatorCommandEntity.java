package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "actuator_command", schema = "irrigation")
public class ActuatorCommandEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID automationDecisionId;
    @Column(nullable = false) private UUID actuatorDeviceId;
    @Column(nullable = false) private String commandType;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String commandPayload;
    @Column(nullable = false) private Instant issuedAt;
    private Instant acknowledgedAt;
    private Instant completedAt;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String failureReason;
    @Column(nullable = false) private String idempotencyKey;
    @Column(nullable = false) private Instant createdAt;

    protected ActuatorCommandEntity() {}

    public static ActuatorCommandEntity create(
            UUID tenantId, UUID decisionId, UUID actuatorDeviceId,
            String commandType, String payload, String idempotencyKey) {
        ActuatorCommandEntity value = new ActuatorCommandEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.automationDecisionId = decisionId;
        value.actuatorDeviceId = actuatorDeviceId;
        value.commandType = commandType;
        value.commandPayload = payload;
        value.issuedAt = Instant.now();
        value.status = "QUEUED";
        value.idempotencyKey = idempotencyKey;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String status() { return status; }
}
