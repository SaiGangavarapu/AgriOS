package com.agrios.platform.weather.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.weather.api.WeatherDtos;
import com.agrios.platform.weather.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeatherService {
    private final WeatherSourceRepository sources;
    private final WeatherLocationRepository locations;
    private final ObservationRepository observations;
    private final ForecastRunRepository forecasts;
    private final WarningRepository warnings;
    private final MonsoonSeasonRepository monsoons;
    private final ClimateRiskAssessmentRepository risks;
    private final FieldRepository fields;
    private final CropCycleRepository cycles;
    private final ObjectMapper mapper;

    public WeatherService(WeatherSourceRepository sources,
                          WeatherLocationRepository locations,
                          ObservationRepository observations,
                          ForecastRunRepository forecasts,
                          WarningRepository warnings,
                          MonsoonSeasonRepository monsoons,
                          ClimateRiskAssessmentRepository risks,
                          FieldRepository fields,
                          CropCycleRepository cycles,
                          ObjectMapper mapper) {
        this.sources = sources;
        this.locations = locations;
        this.observations = observations;
        this.forecasts = forecasts;
        this.warnings = warnings;
        this.monsoons = monsoons;
        this.risks = risks;
        this.fields = fields;
        this.cycles = cycles;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createSource(UUID tenantId, WeatherDtos.SourceRequest request) {
        return sources.save(WeatherSourceEntity.create(
                tenantId, request.code(), request.name(), request.sourceType(),
                request.providerName(), request.baseUrl(),
                request.licenseReference(), request.priority())).id();
    }

    @Transactional
    public UUID createFieldLocation(UUID tenantId,
                                    WeatherDtos.FieldLocationRequest request) {
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        if (locations.findByTenantIdAndFieldId(tenantId, request.fieldId()).isPresent()) {
            throw new ConflictException("WEATHER_LOCATION_ALREADY_EXISTS",
                    "Weather location already exists for field.");
        }
        return locations.save(WeatherLocationEntity.field(
                tenantId, request.fieldId(), request.latitude(),
                request.longitude(), request.elevationMeters(),
                request.gridReference(), request.timezone())).id();
    }

    @Transactional
    public UUID ingestObservation(UUID tenantId,
                                  WeatherDtos.ObservationRequest request) {
        requireSource(tenantId, request.sourceId());
        requireLocation(tenantId, request.locationId());
        return observations.save(ObservationEntity.create(
                tenantId, request.sourceId(), request.locationId(),
                request.observedAt(), request.temperatureC(),
                request.relativeHumidityPercent(), request.rainfallMm(),
                request.windSpeedKph(), request.windDirectionDegrees(),
                request.solarRadiationWm2(), request.pressureHpa(),
                request.soilTemperatureC(), request.sourceQuality(),
                json(request.rawPayload() == null ? Map.of() : request.rawPayload()))).id();
    }

    @Transactional
    public UUID ingestForecast(UUID tenantId,
                               WeatherDtos.ForecastRunRequest request) {
        requireSource(tenantId, request.sourceId());
        requireLocation(tenantId, request.locationId());
        ForecastRunEntity run = ForecastRunEntity.create(
                tenantId, request.sourceId(), request.locationId(),
                request.issuedAt(), request.validFrom(), request.validUntil(),
                request.modelName(), request.modelVersion(), request.confidenceScore());
        request.periods().forEach(p -> run.addPeriod(ForecastPeriodEntity.create(
                p.periodStart(), p.periodEnd(), p.temperatureMinC(),
                p.temperatureMaxC(), p.rainfallMm(),
                p.precipitationProbability(), p.relativeHumidityPercent(),
                p.windSpeedKph(), p.windGustKph(), p.weatherCode())));
        return forecasts.save(run).id();
    }

    @Transactional
    public UUID ingestWarning(UUID tenantId,
                              WeatherDtos.WarningRequest request) {
        requireSource(tenantId, request.sourceId());
        requireLocation(tenantId, request.locationId());
        return warnings.save(WarningEntity.create(
                tenantId, request.sourceId(), request.locationId(),
                request.externalWarningId(), request.warningType(),
                request.severity(), request.headline(), request.description(),
                request.validFrom(), request.validUntil(), request.issuedAt(),
                json(request.rawPayload() == null ? Map.of() : request.rawPayload()))).id();
    }

    @Transactional(readOnly = true)
    public WeatherDtos.ForecastResponse currentForecast(UUID tenantId, UUID fieldId) {
        WeatherLocationEntity location = requireFieldLocation(tenantId, fieldId);
        return forecasts.findByLocationIdAndStatusOrderByIssuedAtDesc(location.id(), "VALID")
                .stream().findFirst()
                .map(WeatherDtos.ForecastResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FORECAST_NOT_FOUND", "Current forecast not found."));
    }

    @Transactional(readOnly = true)
    public List<WeatherDtos.WarningResponse> activeWarnings(UUID tenantId, UUID fieldId) {
        WeatherLocationEntity location = requireFieldLocation(tenantId, fieldId);
        Instant now = Instant.now();
        return warnings.findByLocationIdAndStatusAndValidFromLessThanEqualOrderByIssuedAtDesc(
                        location.id(), "ACTIVE", now)
                .stream()
                .filter(w -> w.validUntil() == null || w.validUntil().isAfter(now))
                .map(WeatherDtos.WarningResponse::from)
                .toList();
    }

    @Transactional
    public WeatherDtos.MonsoonResponse upsertMonsoon(
            UUID tenantId, WeatherDtos.MonsoonRequest request) {
        MonsoonSeasonEntity value = monsoons
                .findByTenantIdAndRegionCodeAndSeasonYearAndMonsoonType(
                        tenantId, request.regionCode(),
                        request.seasonYear(), request.monsoonType())
                .orElseGet(() -> MonsoonSeasonEntity.create(
                        tenantId, request.regionCode(), request.monsoonType(),
                        request.seasonYear(), request.climatologyOnsetDate(),
                        request.climatologyWithdrawalDate(),
                        request.predictedOnsetDate(),
                        request.predictedWithdrawalDate(),
                        request.onsetConfidence(),
                        request.withdrawalConfidence(),
                        json(request.basis() == null ? Map.of() : request.basis())));
        return WeatherDtos.MonsoonResponse.from(monsoons.save(value));
    }

    @Transactional(readOnly = true)
    public WeatherDtos.MonsoonResponse monsoonStatus(
            UUID tenantId, String regionCode, int year, String type) {
        return monsoons.findByTenantIdAndRegionCodeAndSeasonYearAndMonsoonType(
                        tenantId, regionCode, year, type)
                .map(WeatherDtos.MonsoonResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MONSOON_STATUS_NOT_FOUND", "Monsoon status not found."));
    }

    @Transactional
    public WeatherDtos.ClimateRiskResponse assessClimateRisk(
            UUID tenantId, WeatherDtos.ClimateRiskRequest request) {
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        if (request.cropCycleId() != null) {
            cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        }

        WeatherLocationEntity location = requireFieldLocation(tenantId, request.fieldId());
        List<ForecastRunEntity> runs = forecasts
                .findByLocationIdAndStatusOrderByIssuedAtDesc(location.id(), "VALID");

        List<String> reasons = new ArrayList<>();
        BigDecimal maxRain = BigDecimal.ZERO;
        BigDecimal maxTemp = BigDecimal.ZERO;
        BigDecimal totalRain = BigDecimal.ZERO;
        BigDecimal maxProbability = BigDecimal.ZERO;

        if (!runs.isEmpty()) {
            for (ForecastPeriodEntity p : runs.getFirst().periods()) {
                if (p.rainfallMm() != null) {
                    totalRain = totalRain.add(p.rainfallMm());
                    maxRain = maxRain.max(p.rainfallMm());
                }
                if (p.temperatureMaxC() != null) maxTemp = maxTemp.max(p.temperatureMaxC());
                if (p.precipitationProbability() != null) {
                    maxProbability = maxProbability.max(p.precipitationProbability());
                }
            }
        } else {
            reasons.add("FORECAST_MISSING");
        }

        String drought = totalRain.compareTo(new BigDecimal("10")) < 0 ? "HIGH" :
                totalRain.compareTo(new BigDecimal("30")) < 0 ? "MODERATE" : "LOW";
        String flood = maxRain.compareTo(new BigDecimal("100")) > 0 ? "HIGH" :
                maxRain.compareTo(new BigDecimal("50")) > 0 ? "MODERATE" : "LOW";
        String heat = maxTemp.compareTo(new BigDecimal("40")) > 0 ? "HIGH" :
                maxTemp.compareTo(new BigDecimal("35")) > 0 ? "MODERATE" : "LOW";
        String excess = maxProbability.compareTo(new BigDecimal("0.8")) > 0 &&
                totalRain.compareTo(new BigDecimal("80")) > 0 ? "HIGH" : "LOW";

        if (drought.equals("HIGH")) reasons.add("LOW_FORECAST_RAINFALL");
        if (flood.equals("HIGH")) reasons.add("EXTREME_SINGLE_PERIOD_RAINFALL");
        if (heat.equals("HIGH")) reasons.add("EXTREME_MAX_TEMPERATURE");
        if (excess.equals("HIGH")) reasons.add("HIGH_PROBABILITY_EXCESS_RAIN");

        int score = riskScore(drought) + riskScore(flood) + riskScore(heat) +
                riskScore(excess);
        BigDecimal composite = BigDecimal.valueOf(score)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal confidence = runs.isEmpty()
                ? new BigDecimal("0.3000")
                : runs.getFirst().confidenceScore() == null
                    ? new BigDecimal("0.6500")
                    : runs.getFirst().confidenceScore();

        ClimateRiskAssessmentEntity assessment =
                ClimateRiskAssessmentEntity.create(
                        tenantId, request.fieldId(), request.cropCycleId(),
                        request.assessmentWindowStart(),
                        request.assessmentWindowEnd(),
                        drought, flood, heat, "LOW", "LOW", excess,
                        composite, confidence,
                        json(Map.of("forecastRunId",
                                runs.isEmpty() ? "missing" : runs.getFirst().id().toString())),
                        json(reasons));
        return WeatherDtos.ClimateRiskResponse.from(risks.save(assessment));
    }

    private int riskScore(String risk) {
        return switch (risk) {
            case "HIGH" -> 3;
            case "MODERATE" -> 2;
            default -> 0;
        };
    }

    private WeatherSourceEntity requireSource(UUID tenantId, UUID sourceId) {
        return sources.findByIdAndTenantId(sourceId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WEATHER_SOURCE_NOT_FOUND", "Weather source not found."));
    }

    private WeatherLocationEntity requireLocation(UUID tenantId, UUID locationId) {
        return locations.findById(locationId)
                .filter(l -> l.fieldId() == null ||
                        fields.findByIdAndTenantId(l.fieldId(), tenantId).isPresent())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WEATHER_LOCATION_NOT_FOUND", "Weather location not found."));
    }

    private WeatherLocationEntity requireFieldLocation(UUID tenantId, UUID fieldId) {
        return locations.findByTenantIdAndFieldId(tenantId, fieldId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WEATHER_LOCATION_NOT_FOUND", "Weather location not found for field."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize weather data.", 500);
        }
    }
}
