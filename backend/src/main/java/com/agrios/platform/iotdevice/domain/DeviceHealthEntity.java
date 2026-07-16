package com.agrios.platform.iotdevice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "device_health", schema = "iotdevice")
public class DeviceHealthEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID deviceId;
    @Column(nullable = false) private Instant recordedAt;
    private BigDecimal batteryPercent;
    private BigDecimal signalStrengthDbm;
    @Column(name = "internal_temperature_c")
    private BigDecimal internalTemperatureC;
    private Long uptimeSeconds;
    private Long storageFreeBytes;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String errorCodes;
    @Column(nullable = false) private String healthStatus;
    @Column(nullable = false) private Instant createdAt;

    protected DeviceHealthEntity() {}

    public static DeviceHealthEntity create(UUID deviceId, Instant recordedAt,
                                            BigDecimal battery, BigDecimal signal,
                                            BigDecimal temperature, Long uptime,
                                            Long storage, String errors,
                                            String status) {
        DeviceHealthEntity value = new DeviceHealthEntity();
        value.id = UUID.randomUUID();
        value.deviceId = deviceId;
        value.recordedAt = recordedAt;
        value.batteryPercent = battery;
        value.signalStrengthDbm = signal;
        value.internalTemperatureC = temperature;
        value.uptimeSeconds = uptime;
        value.storageFreeBytes = storage;
        value.errorCodes = errors;
        value.healthStatus = status;
        value.createdAt = Instant.now();
        return value;
    }

    public String healthStatus() { return healthStatus; }
}
