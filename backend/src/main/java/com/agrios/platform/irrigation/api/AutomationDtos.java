package com.agrios.platform.irrigation.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public final class AutomationDtos {
    private AutomationDtos() {}

    public record PolicyRequest(
            @NotNull UUID irrigationPlanId,
            @NotBlank String policyName,
            @NotBlank String controlMode,
            UUID moistureStreamId,
            UUID rainStreamId,
            UUID flowStreamId,
            @NotNull UUID actuatorDeviceId,
            BigDecimal moistureTriggerBelow,
            BigDecimal rainSkipThresholdMm,
            @Min(1) int maximumRuntimeMinutes,
            @PositiveOrZero int minimumIntervalMinutes,
            LocalTime allowedStartTime,
            LocalTime allowedEndTime,
            boolean emergencyStopEnabled) {}

    public record EvaluateRequest(UUID irrigationScheduleId) {}
    public record ApproveDecisionRequest() {}
    public record CommandResponse(UUID commandId, String status) {}
}
