package com.agrios.platform.telemetry.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "telemetry_reading", schema = "telemetry")
public class TelemetryReadingEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID streamId;
    @Column(nullable = false) private UUID deviceId;
    @Column(nullable = false) private Instant observedAt;
    @Column(nullable = false) private Instant receivedAt;
    private BigDecimal numericValue;
    private String textValue;
    @Column(nullable = false) private String qualityFlag;
    private Long sequenceNo;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String rawPayload;

    protected TelemetryReadingEntity() {}

    public static TelemetryReadingEntity create(UUID tenantId, UUID streamId,
                                                UUID deviceId, Instant observedAt,
                                                BigDecimal numericValue,
                                                String textValue,
                                                String qualityFlag,
                                                Long sequenceNo,
                                                String rawPayload) {
        TelemetryReadingEntity value = new TelemetryReadingEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.streamId = streamId;
        value.deviceId = deviceId;
        value.observedAt = observedAt;
        value.receivedAt = Instant.now();
        value.numericValue = numericValue;
        value.textValue = textValue;
        value.qualityFlag = qualityFlag;
        value.sequenceNo = sequenceNo;
        value.rawPayload = rawPayload;
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal numericValue() { return numericValue; }
    public Instant observedAt() { return observedAt; }
    public String qualityFlag() { return qualityFlag; }
}
