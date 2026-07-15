package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "financial_event", schema = "finance")
public class FinancialEventEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID farmerId;
    @Column(nullable = false) private String eventType;
    @Column(nullable = false) private String sourceModule;
    @Column(nullable = false) private String sourceReferenceType;
    private UUID sourceReferenceId;
    @Column(nullable = false) private LocalDate eventDate;
    @Column(nullable = false) private String currencyCode;
    @Column(nullable = false) private BigDecimal amount;
    @Column(nullable = false) private String direction;
    @Column(nullable = false) private String description;
    private UUID fieldId;
    private UUID cropCycleId;
    private String costCenterCode;
    @Column(nullable = false) private String idempotencyKey;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    private Instant postedAt;

    protected FinancialEventEntity() {}

    public static FinancialEventEntity create(
            UUID tenantId, UUID farmerId, String eventType,
            String sourceModule, String sourceReferenceType,
            UUID sourceReferenceId, LocalDate eventDate,
            String currencyCode, BigDecimal amount, String direction,
            String description, UUID fieldId, UUID cropCycleId,
            String costCenterCode, String idempotencyKey, String payload) {
        FinancialEventEntity value = new FinancialEventEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.eventType = eventType;
        value.sourceModule = sourceModule;
        value.sourceReferenceType = sourceReferenceType;
        value.sourceReferenceId = sourceReferenceId;
        value.eventDate = eventDate;
        value.currencyCode = currencyCode;
        value.amount = amount;
        value.direction = direction;
        value.description = description;
        value.fieldId = fieldId;
        value.cropCycleId = cropCycleId;
        value.costCenterCode = costCenterCode;
        value.idempotencyKey = idempotencyKey;
        value.payload = payload;
        value.status = "VALIDATED";
        value.createdAt = Instant.now();
        return value;
    }

    public void markPosted() {
        status = "POSTED";
        postedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public String eventType() { return eventType; }
    public String sourceModule() { return sourceModule; }
    public LocalDate eventDate() { return eventDate; }
    public BigDecimal amount() { return amount; }
    public String direction() { return direction; }
    public UUID fieldId() { return fieldId; }
    public UUID cropCycleId() { return cropCycleId; }
    public String costCenterCode() { return costCenterCode; }
    public String status() { return status; }
}
