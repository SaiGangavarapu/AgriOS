package com.agrios.platform.telemetry.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class TelemetryDtos {
    private TelemetryDtos() {}

    public record StreamRequest(
            @NotNull UUID deviceId,
            @NotBlank String streamCode,
            @NotBlank String measurementType,
            @NotBlank String unitCode,
            @Positive Integer samplingIntervalSeconds,
            BigDecimal expectedMin,
            BigDecimal expectedMax) {}

    public record ReadingRequest(
            @NotNull UUID streamId,
            @NotNull Instant observedAt,
            BigDecimal numericValue,
            String textValue,
            @NotBlank String qualityFlag,
            Long sequenceNo,
            Map<String,Object> rawPayload) {
        @AssertTrue(message = "numericValue or textValue is required")
        public boolean valuePresent() {
            return numericValue != null || (textValue != null && !textValue.isBlank());
        }
    }

    public record LatestReadingResponse(
            UUID readingId, BigDecimal numericValue,
            Instant observedAt, String qualityFlag) {}
}
