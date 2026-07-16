package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "observation", schema = "weather")
public class ObservationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID sourceId;
    @Column(nullable = false) private UUID locationId;
    @Column(nullable = false) private Instant observedAt;
    @Column(name = "temperature_c")
    private BigDecimal temperatureC;
    private BigDecimal relativeHumidityPercent;
    private BigDecimal rainfallMm;
    private BigDecimal windSpeedKph;
    private BigDecimal windDirectionDegrees;
    private BigDecimal solarRadiationWm2;
    private BigDecimal pressureHpa;
    @Column(name = "soil_temperature_c")
    private BigDecimal soilTemperatureC;
    @Column(nullable = false) private String sourceQuality;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String rawPayload;
    @Column(nullable = false) private Instant receivedAt;

    protected ObservationEntity() {}

    public static ObservationEntity create(UUID tenantId, UUID sourceId, UUID locationId,
                                           Instant observedAt, BigDecimal temperature,
                                           BigDecimal humidity, BigDecimal rainfall,
                                           BigDecimal windSpeed, BigDecimal windDirection,
                                           BigDecimal solar, BigDecimal pressure,
                                           BigDecimal soilTemp, String quality,
                                           String rawPayload) {
        ObservationEntity value = new ObservationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceId = sourceId;
        value.locationId = locationId;
        value.observedAt = observedAt;
        value.temperatureC = temperature;
        value.relativeHumidityPercent = humidity;
        value.rainfallMm = rainfall;
        value.windSpeedKph = windSpeed;
        value.windDirectionDegrees = windDirection;
        value.solarRadiationWm2 = solar;
        value.pressureHpa = pressure;
        value.soilTemperatureC = soilTemp;
        value.sourceQuality = quality;
        value.rawPayload = rawPayload;
        value.receivedAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public Instant observedAt() { return observedAt; }
    public BigDecimal temperatureC() { return temperatureC; }
    public BigDecimal rainfallMm() { return rainfallMm; }
    public BigDecimal relativeHumidityPercent() { return relativeHumidityPercent; }
}
