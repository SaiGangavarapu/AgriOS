package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "weather_location", schema = "weather")
public class WeatherLocationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID fieldId;
    @Column(nullable = false) private String locationType;
    @Column(nullable = false) private BigDecimal latitude;
    @Column(nullable = false) private BigDecimal longitude;
    private BigDecimal elevationMeters;
    private String gridReference;
    @Column(nullable = false) private String timezone;
    @Column(nullable = false) private Instant createdAt;

    protected WeatherLocationEntity() {}

    public static WeatherLocationEntity field(UUID tenantId, UUID fieldId,
                                              BigDecimal latitude, BigDecimal longitude,
                                              BigDecimal elevation, String grid,
                                              String timezone) {
        WeatherLocationEntity value = new WeatherLocationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.locationType = "FIELD";
        value.latitude = latitude;
        value.longitude = longitude;
        value.elevationMeters = elevation;
        value.gridReference = grid;
        value.timezone = timezone;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public BigDecimal latitude() { return latitude; }
    public BigDecimal longitude() { return longitude; }
    public String timezone() { return timezone; }
}
