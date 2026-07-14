package com.agrios.platform.telemetry.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.iotdevice.domain.*;
import com.agrios.platform.telemetry.api.TelemetryDtos;
import com.agrios.platform.telemetry.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryService {
    private final TelemetryStreamRepository streams;
    private final TelemetryReadingRepository readings;
    private final TelemetryAlertRepository alerts;
    private final DeviceRepository devices;
    private final ObjectMapper mapper;

    public TelemetryService(TelemetryStreamRepository streams,
                            TelemetryReadingRepository readings,
                            TelemetryAlertRepository alerts,
                            DeviceRepository devices,
                            ObjectMapper mapper) {
        this.streams = streams;
        this.readings = readings;
        this.alerts = alerts;
        this.devices = devices;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createStream(UUID tenantId, TelemetryDtos.StreamRequest request) {
        devices.findByIdAndTenantId(request.deviceId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DEVICE_NOT_FOUND", "Device not found."));
        return streams.save(TelemetryStreamEntity.create(
                tenantId, request.deviceId(), request.streamCode(),
                request.measurementType(), request.unitCode(),
                request.samplingIntervalSeconds(),
                request.expectedMin(), request.expectedMax())).id();
    }

    @Transactional
    public UUID ingest(UUID tenantId, TelemetryDtos.ReadingRequest request) {
        TelemetryStreamEntity stream = streams.findByIdAndTenantId(request.streamId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TELEMETRY_STREAM_NOT_FOUND", "Telemetry stream not found."));
        String quality = determineQuality(stream, request.numericValue(), request.qualityFlag());
        TelemetryReadingEntity reading = readings.save(TelemetryReadingEntity.create(
                tenantId, stream.id(), stream.deviceId(),
                request.observedAt(), request.numericValue(),
                request.textValue(), quality, request.sequenceNo(),
                json(request.rawPayload() == null ? Map.of() : request.rawPayload())));
        if ("OUT_OF_RANGE".equals(quality)) {
            alerts.save(TelemetryAlertEntity.create(
                    tenantId, stream.deviceId(), stream.id(),
                    "OUT_OF_RANGE", "WARNING",
                    "Telemetry value is outside configured bounds.",
                    json(Map.of("readingId", reading.id(), "streamId", stream.id()))));
        }
        return reading.id();
    }

    @Transactional(readOnly = true)
    public TelemetryDtos.LatestReadingResponse latest(UUID tenantId, UUID streamId) {
        streams.findByIdAndTenantId(streamId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TELEMETRY_STREAM_NOT_FOUND", "Telemetry stream not found."));
        TelemetryReadingEntity value = readings.findTopByStreamIdOrderByObservedAtDesc(streamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TELEMETRY_READING_NOT_FOUND", "Telemetry reading not found."));
        return new TelemetryDtos.LatestReadingResponse(
                value.id(), value.numericValue(),
                value.observedAt(), value.qualityFlag());
    }

    @Transactional(readOnly = true)
    public List<TelemetryAlertEntity> openAlerts(UUID tenantId) {
        return alerts.findByTenantIdAndStatusOrderByTriggeredAtDesc(tenantId, "OPEN");
    }

    private String determineQuality(TelemetryStreamEntity stream,
                                    BigDecimal value, String requested) {
        if (value == null) return requested;
        if (stream.expectedMin() != null && value.compareTo(stream.expectedMin()) < 0) {
            return "OUT_OF_RANGE";
        }
        if (stream.expectedMax() != null && value.compareTo(stream.expectedMax()) > 0) {
            return "OUT_OF_RANGE";
        }
        return requested;
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize telemetry data.", 500);
        }
    }
}
