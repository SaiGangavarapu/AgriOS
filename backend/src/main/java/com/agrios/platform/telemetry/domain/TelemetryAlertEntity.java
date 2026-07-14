package com.agrios.platform.telemetry.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "telemetry_alert", schema = "telemetry")
public class TelemetryAlertEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID deviceId;
    private UUID streamId;
    @Column(nullable = false) private String alertType;
    @Column(nullable = false) private String severity;
    @Column(nullable = false) private Instant triggeredAt;
    private Instant clearedAt;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private String message;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String context;
    private UUID acknowledgedBy;
    private Instant acknowledgedAt;
    @Column(nullable = false) private Instant createdAt;

    protected TelemetryAlertEntity() {}

    public static TelemetryAlertEntity create(UUID tenantId, UUID deviceId,
                                              UUID streamId, String type,
                                              String severity, String message,
                                              String context) {
        TelemetryAlertEntity value = new TelemetryAlertEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.deviceId = deviceId;
        value.streamId = streamId;
        value.alertType = type;
        value.severity = severity;
        value.triggeredAt = Instant.now();
        value.status = "OPEN";
        value.message = message;
        value.context = context;
        value.createdAt = Instant.now();
        return value;
    }

    public void acknowledge(UUID actorId) {
        status = "ACKNOWLEDGED";
        acknowledgedBy = actorId;
        acknowledgedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String severity() { return severity; }
    public String message() { return message; }
}
