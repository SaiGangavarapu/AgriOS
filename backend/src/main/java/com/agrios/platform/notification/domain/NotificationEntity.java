package com.agrios.platform.notification.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notification", schema = "notification")
public class NotificationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID farmerId;
    private UUID recipientUserId;
    private UUID advisoryId;
    private UUID reviewCaseId;
    @Column(nullable = false) private String notificationType;
    @Column(nullable = false) private String channel;
    @Column(nullable = false) private String language;
    private String recipientAddress;
    private String subject;
    @Column(nullable = false, columnDefinition = "text") private String body;
    @Column(nullable = false) private String priority;
    private Instant scheduledAt;
    @Column(nullable = false) private String status;
    private String providerReference;
    @Column(nullable = false) private int attemptCount;
    private Instant lastAttemptAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private Instant readAt;
    private String failureCode;
    @Column(columnDefinition = "text") private String failureMessage;
    @Column(nullable = false) private String idempotencyKey;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;
    @Column(nullable = false) private Instant createdAt;

    protected NotificationEntity() {}

    public static NotificationEntity create(
            UUID tenantId, UUID farmerId, UUID recipientUserId,
            UUID advisoryId, UUID reviewCaseId, String type,
            String channel, String language, String address,
            String subject, String body, String priority,
            Instant scheduledAt, String idempotencyKey,
            String metadata) {
        NotificationEntity value = new NotificationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.recipientUserId = recipientUserId;
        value.advisoryId = advisoryId;
        value.reviewCaseId = reviewCaseId;
        value.notificationType = type;
        value.channel = channel;
        value.language = language;
        value.recipientAddress = address;
        value.subject = subject;
        value.body = body;
        value.priority = priority;
        value.scheduledAt = scheduledAt;
        value.status = scheduledAt == null ? "QUEUED" : "SCHEDULED";
        value.idempotencyKey = idempotencyKey;
        value.metadata = metadata;
        value.createdAt = Instant.now();
        return value;
    }

    public void markSending() {
        status = "SENDING";
        attemptCount++;
        lastAttemptAt = Instant.now();
    }

    public void markSent(String providerReference) {
        status = "SENT";
        this.providerReference = providerReference;
        sentAt = Instant.now();
    }

    public void markDelivered() {
        status = "DELIVERED";
        deliveredAt = Instant.now();
    }

    public void markFailed(String code, String message) {
        status = "FAILED";
        failureCode = code;
        failureMessage = message;
    }

    public void markRead() {
        status = "READ";
        readAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public String notificationType() { return notificationType; }
    public String channel() { return channel; }
    public String language() { return language; }
    public String subject() { return subject; }
    public String body() { return body; }
    public String priority() { return priority; }
    public String status() { return status; }
    public int attemptCount() { return attemptCount; }
    public Instant sentAt() { return sentAt; }
    public Instant deliveredAt() { return deliveredAt; }
    public Instant readAt() { return readAt; }
}
