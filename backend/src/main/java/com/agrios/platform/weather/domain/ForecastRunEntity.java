package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
@Entity
@Table(name = "forecast_run", schema = "weather")
public class ForecastRunEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID sourceId;
    @Column(nullable = false) private UUID locationId;
    @Column(nullable = false) private Instant issuedAt;
    @Column(nullable = false) private Instant validFrom;
    @Column(nullable = false) private Instant validUntil;
    private String modelName;
    private String modelVersion;
    private BigDecimal confidenceScore;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant receivedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "forecast_run_id", nullable = false)
    private List<ForecastPeriodEntity> periods = new ArrayList<>();

    protected ForecastRunEntity() {}

    public static ForecastRunEntity create(UUID tenantId, UUID sourceId, UUID locationId,
                                           Instant issuedAt, Instant validFrom, Instant validUntil,
                                           String modelName, String modelVersion,
                                           BigDecimal confidenceScore) {
        ForecastRunEntity value = new ForecastRunEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceId = sourceId;
        value.locationId = locationId;
        value.issuedAt = issuedAt;
        value.validFrom = validFrom;
        value.validUntil = validUntil;
        value.modelName = modelName;
        value.modelVersion = modelVersion;
        value.confidenceScore = confidenceScore;
        value.status = "VALID";
        value.receivedAt = Instant.now();
        return value;
    }

    public void addPeriod(ForecastPeriodEntity period) { periods.add(period); }
    public UUID id() { return id; }
    public Instant issuedAt() { return issuedAt; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public List<ForecastPeriodEntity> periods() { return List.copyOf(periods); }
}
