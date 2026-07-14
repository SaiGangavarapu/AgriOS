package com.agrios.platform.weather.api;

import com.agrios.platform.weather.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class WeatherDtos {
    private WeatherDtos() {}

    public record SourceRequest(
            @NotBlank String code,
            @NotBlank String name,
            @NotBlank String sourceType,
            @NotBlank String providerName,
            String baseUrl,
            String licenseReference,
            @PositiveOrZero int priority) {}

    public record FieldLocationRequest(
            @NotNull UUID fieldId,
            @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
            @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude,
            BigDecimal elevationMeters,
            String gridReference,
            @NotBlank String timezone) {}

    public record ObservationRequest(
            @NotNull UUID sourceId,
            @NotNull UUID locationId,
            @NotNull Instant observedAt,
            BigDecimal temperatureC,
            BigDecimal relativeHumidityPercent,
            BigDecimal rainfallMm,
            BigDecimal windSpeedKph,
            BigDecimal windDirectionDegrees,
            BigDecimal solarRadiationWm2,
            BigDecimal pressureHpa,
            BigDecimal soilTemperatureC,
            @NotBlank String sourceQuality,
            Map<String, Object> rawPayload) {}

    public record ForecastPeriodRequest(
            @NotNull Instant periodStart,
            @NotNull Instant periodEnd,
            BigDecimal temperatureMinC,
            BigDecimal temperatureMaxC,
            BigDecimal rainfallMm,
            BigDecimal precipitationProbability,
            BigDecimal relativeHumidityPercent,
            BigDecimal windSpeedKph,
            BigDecimal windGustKph,
            String weatherCode) {}

    public record ForecastRunRequest(
            @NotNull UUID sourceId,
            @NotNull UUID locationId,
            @NotNull Instant issuedAt,
            @NotNull Instant validFrom,
            @NotNull Instant validUntil,
            String modelName,
            String modelVersion,
            BigDecimal confidenceScore,
            @NotEmpty List<@Valid ForecastPeriodRequest> periods) {}

    public record WarningRequest(
            @NotNull UUID sourceId,
            @NotNull UUID locationId,
            String externalWarningId,
            @NotBlank String warningType,
            @NotBlank String severity,
            @NotBlank String headline,
            String description,
            @NotNull Instant validFrom,
            Instant validUntil,
            @NotNull Instant issuedAt,
            Map<String, Object> rawPayload) {}

    public record MonsoonRequest(
            @NotBlank String regionCode,
            @NotBlank String monsoonType,
            @Min(2000) int seasonYear,
            LocalDate climatologyOnsetDate,
            LocalDate climatologyWithdrawalDate,
            LocalDate predictedOnsetDate,
            LocalDate predictedWithdrawalDate,
            BigDecimal onsetConfidence,
            BigDecimal withdrawalConfidence,
            Map<String,Object> basis) {}

    public record ClimateRiskRequest(
            @NotNull UUID fieldId,
            UUID cropCycleId,
            @NotNull LocalDate assessmentWindowStart,
            @NotNull LocalDate assessmentWindowEnd) {}

    public record ForecastPeriodResponse(
            Instant periodStart, Instant periodEnd,
            BigDecimal rainfallMm, BigDecimal precipitationProbability,
            BigDecimal temperatureMaxC) {
        public static ForecastPeriodResponse from(ForecastPeriodEntity value) {
            return new ForecastPeriodResponse(value.periodStart(), value.periodEnd(),
                    value.rainfallMm(), value.precipitationProbability(),
                    value.temperatureMaxC());
        }
    }

    public record ForecastResponse(
            UUID forecastRunId, Instant issuedAt,
            BigDecimal confidenceScore,
            List<ForecastPeriodResponse> periods) {
        public static ForecastResponse from(ForecastRunEntity value) {
            return new ForecastResponse(value.id(), value.issuedAt(),
                    value.confidenceScore(),
                    value.periods().stream().map(ForecastPeriodResponse::from).toList());
        }
    }

    public record WarningResponse(
            UUID id, String warningType, String severity,
            String headline, Instant validFrom, Instant validUntil) {
        public static WarningResponse from(WarningEntity value) {
            return new WarningResponse(value.id(), value.warningType(),
                    value.severity(), value.headline(),
                    value.validFrom(), value.validUntil());
        }
    }

    public record MonsoonResponse(
            UUID id, String regionCode, String monsoonType,
            int seasonYear, LocalDate predictedOnsetDate,
            LocalDate predictedWithdrawalDate,
            BigDecimal onsetConfidence, String status,
            String basisJson) {
        public static MonsoonResponse from(MonsoonSeasonEntity value) {
            return new MonsoonResponse(value.id(), value.regionCode(),
                    value.monsoonType(), value.seasonYear(),
                    value.predictedOnsetDate(),
                    value.predictedWithdrawalDate(),
                    value.onsetConfidence(), value.status(),
                    value.basisJson());
        }
    }

    public record ClimateRiskResponse(
            UUID id, String droughtRisk, String floodRisk,
            String heatRisk, String coldRisk, String windRisk,
            String excessRainRisk, BigDecimal compositeScore,
            BigDecimal confidenceScore, String reasonCodesJson) {
        public static ClimateRiskResponse from(ClimateRiskAssessmentEntity value) {
            return new ClimateRiskResponse(value.id(), value.droughtRisk(),
                    value.floodRisk(), value.heatRisk(), value.coldRisk(),
                    value.windRisk(), value.excessRainRisk(),
                    value.compositeScore(), value.confidenceScore(),
                    value.reasonCodes());
        }
    }
}
