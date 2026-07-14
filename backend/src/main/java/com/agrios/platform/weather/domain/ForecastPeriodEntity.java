package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "forecast_period", schema = "weather")
public class ForecastPeriodEntity {
    @Id private UUID id;
    @Column(name = "forecast_run_id", insertable = false, updatable = false)
    private UUID forecastRunId;
    @Column(nullable = false) private Instant periodStart;
    @Column(nullable = false) private Instant periodEnd;
    private BigDecimal temperatureMinC;
    private BigDecimal temperatureMaxC;
    private BigDecimal rainfallMm;
    private BigDecimal precipitationProbability;
    private BigDecimal relativeHumidityPercent;
    private BigDecimal windSpeedKph;
    private BigDecimal windGustKph;
    private String weatherCode;
    @Column(nullable = false) private Instant createdAt;

    protected ForecastPeriodEntity() {}

    public static ForecastPeriodEntity create(Instant start, Instant end,
                                              BigDecimal minTemp, BigDecimal maxTemp,
                                              BigDecimal rainfall, BigDecimal probability,
                                              BigDecimal humidity, BigDecimal windSpeed,
                                              BigDecimal gust, String weatherCode) {
        ForecastPeriodEntity value = new ForecastPeriodEntity();
        value.id = UUID.randomUUID();
        value.periodStart = start;
        value.periodEnd = end;
        value.temperatureMinC = minTemp;
        value.temperatureMaxC = maxTemp;
        value.rainfallMm = rainfall;
        value.precipitationProbability = probability;
        value.relativeHumidityPercent = humidity;
        value.windSpeedKph = windSpeed;
        value.windGustKph = gust;
        value.weatherCode = weatherCode;
        value.createdAt = Instant.now();
        return value;
    }

    public Instant periodStart() { return periodStart; }
    public Instant periodEnd() { return periodEnd; }
    public BigDecimal rainfallMm() { return rainfallMm; }
    public BigDecimal precipitationProbability() { return precipitationProbability; }
    public BigDecimal temperatureMaxC() { return temperatureMaxC; }
}
