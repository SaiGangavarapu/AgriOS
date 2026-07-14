package com.agrios.platform.notification.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notification_attempt", schema = "notification")
public class NotificationAttemptEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID notificationId;
    @Column(nullable = false) private int attemptNo;
    @Column(nullable = false) private Instant attemptedAt;
    private String providerName;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String requestSnapshot;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String responseSnapshot;
    @Column(nullable = false) private String resultStatus;
    private String errorCode;
    @Column(columnDefinition = "text") private String errorMessage;
    private Instant nextRetryAt;

    protected NotificationAttemptEntity() {}

    public static NotificationAttemptEntity create(
            UUID notificationId, int attemptNo, String providerName,
            String requestSnapshot, String responseSnapshot,
            String resultStatus, String errorCode,
            String errorMessage, Instant nextRetryAt) {
        NotificationAttemptEntity value = new NotificationAttemptEntity();
        value.id = UUID.randomUUID();
        value.notificationId = notificationId;
        value.attemptNo = attemptNo;
        value.attemptedAt = Instant.now();
        value.providerName = providerName;
        value.requestSnapshot = requestSnapshot;
        value.responseSnapshot = responseSnapshot;
        value.resultStatus = resultStatus;
        value.errorCode = errorCode;
        value.errorMessage = errorMessage;
        value.nextRetryAt = nextRetryAt;
        return value;
    }
}
