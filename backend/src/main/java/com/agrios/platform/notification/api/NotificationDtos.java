package com.agrios.platform.notification.api;

import com.agrios.platform.notification.domain.NotificationEntity;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;

public final class NotificationDtos {
    private NotificationDtos() {}

    public record CreateRequest(
            UUID farmerId,
            UUID recipientUserId,
            UUID advisoryId,
            UUID reviewCaseId,
            @NotBlank String notificationType,
            @NotBlank String channel,
            @NotBlank String language,
            String recipientAddress,
            String subject,
            @NotBlank String body,
            @NotBlank String priority,
            Instant scheduledAt,
            @NotBlank String idempotencyKey,
            Map<String,Object> metadata) {}

    public record DeliveryResultRequest(
            @NotBlank String resultStatus,
            String providerName,
            String providerReference,
            String errorCode,
            String errorMessage,
            Map<String,Object> responseSnapshot,
            Instant nextRetryAt) {}

    public record Response(
            UUID id, UUID farmerId, String notificationType,
            String channel, String language, String subject,
            String body, String priority, String status,
            int attemptCount, Instant sentAt,
            Instant deliveredAt, Instant readAt) {
        public static Response from(NotificationEntity value) {
            return new Response(value.id(), value.farmerId(),
                    value.notificationType(), value.channel(),
                    value.language(), value.subject(), value.body(),
                    value.priority(), value.status(),
                    value.attemptCount(), value.sentAt(),
                    value.deliveredAt(), value.readAt());
        }
    }
}
