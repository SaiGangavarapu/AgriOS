package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "weather_source", schema = "weather")
public class WeatherSourceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String sourceType;
    @Column(nullable = false) private String providerName;
    private String baseUrl;
    private String licenseReference;
    @Column(nullable = false) private int priority;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected WeatherSourceEntity() {}

    public static WeatherSourceEntity create(UUID tenantId, String code,
                                             String name, String sourceType,
                                             String providerName, String baseUrl,
                                             String licenseReference, int priority) {
        WeatherSourceEntity value = new WeatherSourceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.code = code.trim().toUpperCase();
        value.name = name.trim();
        value.sourceType = sourceType;
        value.providerName = providerName;
        value.baseUrl = baseUrl;
        value.licenseReference = licenseReference;
        value.priority = priority;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String code() { return code; }
    public String name() { return name; }
    public String sourceType() { return sourceType; }
    public String providerName() { return providerName; }
    public int priority() { return priority; }
    public String status() { return status; }
}
