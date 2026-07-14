package com.agrios.platform.notification.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.notification.api.NotificationDtos;
import com.agrios.platform.notification.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    private final NotificationRepository notifications;
    private final NotificationAttemptRepository attempts;
    private final FarmerRepository farmers;
    private final ObjectMapper mapper;

    public NotificationService(NotificationRepository notifications,
                               NotificationAttemptRepository attempts,
                               FarmerRepository farmers,
                               ObjectMapper mapper) {
        this.notifications = notifications;
        this.attempts = attempts;
        this.farmers = farmers;
        this.mapper = mapper;
    }

    @Transactional
    public NotificationDtos.Response create(
            UUID tenantId, NotificationDtos.CreateRequest request) {
        if (request.farmerId() != null) {
            farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARMER_NOT_FOUND", "Farmer not found."));
        }
        if (notifications.existsByTenantIdAndIdempotencyKey(
                tenantId, request.idempotencyKey())) {
            throw new ConflictException("NOTIFICATION_DUPLICATE",
                    "Notification idempotency key already exists.");
        }
        NotificationEntity value = NotificationEntity.create(
                tenantId, request.farmerId(), request.recipientUserId(),
                request.advisoryId(), request.reviewCaseId(),
                request.notificationType(), request.channel(),
                request.language(), request.recipientAddress(),
                request.subject(), request.body(), request.priority(),
                request.scheduledAt(), request.idempotencyKey(),
                json(request.metadata() == null ? Map.of() : request.metadata()));
        return NotificationDtos.Response.from(notifications.save(value));
    }

    @Transactional
    public NotificationDtos.Response markSending(UUID tenantId, UUID notificationId) {
        NotificationEntity value = requireNotification(tenantId, notificationId);
        value.markSending();
        return NotificationDtos.Response.from(value);
    }

    @Transactional
    public NotificationDtos.Response deliveryResult(
            UUID tenantId, UUID notificationId,
            NotificationDtos.DeliveryResultRequest request) {
        NotificationEntity value = requireNotification(tenantId, notificationId);

        String result = request.resultStatus();
        if ("SENT".equals(result)) {
            value.markSent(request.providerReference());
        } else if ("DELIVERED".equals(result)) {
            value.markDelivered();
        } else if ("FAILED".equals(result) || "RETRY_SCHEDULED".equals(result)) {
            value.markFailed(request.errorCode(), request.errorMessage());
        } else {
            throw new BusinessException("NOTIFICATION_RESULT_INVALID",
                    "Unsupported notification result.", 422);
        }

        attempts.save(NotificationAttemptEntity.create(
                value.id(), value.attemptCount(),
                request.providerName(), "{}",
                json(request.responseSnapshot() == null
                        ? Map.of() : request.responseSnapshot()),
                result, request.errorCode(), request.errorMessage(),
                request.nextRetryAt()));

        return NotificationDtos.Response.from(value);
    }

    @Transactional
    public NotificationDtos.Response markRead(UUID tenantId, UUID notificationId) {
        NotificationEntity value = requireNotification(tenantId, notificationId);
        value.markRead();
        return NotificationDtos.Response.from(value);
    }

    @Transactional(readOnly = true)
    public List<NotificationDtos.Response> farmerNotifications(
            UUID tenantId, UUID farmerId) {
        farmers.findByIdAndTenantId(farmerId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        return notifications.findByTenantIdAndFarmerIdOrderByCreatedAtDesc(
                        tenantId, farmerId)
                .stream().map(NotificationDtos.Response::from).toList();
    }

    private NotificationEntity requireNotification(UUID tenantId, UUID id) {
        return notifications.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "NOTIFICATION_NOT_FOUND", "Notification not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize notification data.", 500);
        }
    }
}
