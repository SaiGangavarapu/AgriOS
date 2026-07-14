package com.agrios.platform.iotdevice.api;

import com.agrios.platform.iotdevice.domain.DeviceEntity;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class IoTDeviceDtos {
    private IoTDeviceDtos() {}

    public record RegisterRequest(
            @NotBlank String deviceUid,
            @NotBlank String deviceType,
            String manufacturer,
            String model,
            String firmwareVersion,
            @NotBlank String communicationProtocol,
            @NotBlank String authenticationMode,
            Map<String,Object> metadata) {}

    public record AssignmentRequest(
            @NotBlank String assignmentType,
            @NotNull UUID assignedEntityId,
            String notes) {}

    public record HealthRequest(
            @NotNull Instant recordedAt,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal batteryPercent,
            BigDecimal signalStrengthDbm,
            BigDecimal internalTemperatureC,
            @PositiveOrZero Long uptimeSeconds,
            @PositiveOrZero Long storageFreeBytes,
            List<String> errorCodes,
            @NotBlank String healthStatus) {}

    public record HeartbeatRequest(@NotNull Instant seenAt) {}

    public record DeviceResponse(
            UUID id, String deviceUid, String deviceType,
            String communicationProtocol, String status,
            Instant lastSeenAt, long version) {
        public static DeviceResponse from(DeviceEntity value) {
            return new DeviceResponse(value.id(), value.deviceUid(),
                    value.deviceType(), value.communicationProtocol(),
                    value.status(), value.lastSeenAt(), value.version());
        }
    }
}
