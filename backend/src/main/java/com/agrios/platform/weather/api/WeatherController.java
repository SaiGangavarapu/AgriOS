package com.agrios.platform.weather.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.weather.application.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class WeatherController {
    private final WeatherService service;
    private final TenantContextResolver tenants;

    public WeatherController(WeatherService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/weather/sources")
    Map<String, UUID> createSource(
            @Valid @RequestBody WeatherDtos.SourceRequest body,
            HttpServletRequest request) {
        return Map.of("sourceId", service.createSource(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/weather/field-locations")
    Map<String, UUID> createFieldLocation(
            @Valid @RequestBody WeatherDtos.FieldLocationRequest body,
            HttpServletRequest request) {
        return Map.of("locationId", service.createFieldLocation(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/integration/weather/observations")
    Map<String, UUID> ingestObservation(
            @Valid @RequestBody WeatherDtos.ObservationRequest body,
            HttpServletRequest request) {
        return Map.of("observationId", service.ingestObservation(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/integration/weather/forecasts")
    Map<String, UUID> ingestForecast(
            @Valid @RequestBody WeatherDtos.ForecastRunRequest body,
            HttpServletRequest request) {
        return Map.of("forecastRunId", service.ingestForecast(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/integration/weather/warnings")
    Map<String, UUID> ingestWarning(
            @Valid @RequestBody WeatherDtos.WarningRequest body,
            HttpServletRequest request) {
        return Map.of("warningId", service.ingestWarning(
                tenants.resolve(request).tenantId(), body));
    }

    @GetMapping("/weather/fields/{fieldId}/forecast")
    WeatherDtos.ForecastResponse forecast(
            @PathVariable UUID fieldId, HttpServletRequest request) {
        return service.currentForecast(
                tenants.resolve(request).tenantId(), fieldId);
    }

    @GetMapping("/weather/fields/{fieldId}/warnings")
    List<WeatherDtos.WarningResponse> warnings(
            @PathVariable UUID fieldId, HttpServletRequest request) {
        return service.activeWarnings(
                tenants.resolve(request).tenantId(), fieldId);
    }

    @PostMapping("/weather/monsoon/status")
    WeatherDtos.MonsoonResponse upsertMonsoon(
            @Valid @RequestBody WeatherDtos.MonsoonRequest body,
            HttpServletRequest request) {
        return service.upsertMonsoon(
                tenants.resolve(request).tenantId(), body);
    }

    @GetMapping("/weather/monsoon/status")
    WeatherDtos.MonsoonResponse monsoonStatus(
            @RequestParam String regionCode,
            @RequestParam int seasonYear,
            @RequestParam String monsoonType,
            HttpServletRequest request) {
        return service.monsoonStatus(
                tenants.resolve(request).tenantId(),
                regionCode, seasonYear, monsoonType);
    }

    @PostMapping("/weather/climate-risk-assessments")
    WeatherDtos.ClimateRiskResponse climateRisk(
            @Valid @RequestBody WeatherDtos.ClimateRiskRequest body,
            HttpServletRequest request) {
        return service.assessClimateRisk(
                tenants.resolve(request).tenantId(), body);
    }
}
