package com.agrios.platform.telemetry.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "telemetry_stream", schema = "telemetry")
public class TelemetryStreamEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID deviceId;
    @Column(nullable = false) private String streamCode;
    @Column(nullable = false) private String measurementType;
    @Column(nullable = false) private String unitCode;
    private Integer samplingIntervalSeconds;
    private BigDecimal expectedMin;
    private BigDecimal expectedMax;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected TelemetryStreamEntity() {}

    public static TelemetryStreamEntity create(UUID tenantId, UUID deviceId,
                                               String code, String measurementType,
                                               String unit, Integer interval,
                                               BigDecimal expectedMin,
                                               BigDecimal expectedMax) {
        TelemetryStreamEntity value = new TelemetryStreamEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.deviceId = deviceId;
        value.streamCode = code;
        value.measurementType = measurementType;
        value.unitCode = unit;
        value.samplingIntervalSeconds = interval;
        value.expectedMin = expectedMin;
        value.expectedMax = expectedMax;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID deviceId() { return deviceId; }
    public String measurementType() { return measurementType; }
    public String unitCode() { return unitCode; }
    public BigDecimal expectedMin() { return expectedMin; }
    public BigDecimal expectedMax() { return expectedMax; }
}
