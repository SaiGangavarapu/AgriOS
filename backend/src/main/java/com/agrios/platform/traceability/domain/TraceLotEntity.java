package com.agrios.platform.traceability.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "trace_lot", schema = "traceability")
public class TraceLotEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String lotCode;
    @Column(nullable = false) private UUID harvestBatchId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private UUID fieldId;
    private UUID qualityAssessmentId;
    private String qualityGrade;
    @Column(nullable = false) private BigDecimal quantity;
    @Column(nullable = false) private String quantityUnit;
    private Instant packedAt;
    private LocalDate expiryDate;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private String qrToken;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String traceSnapshot;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected TraceLotEntity() {}

    public static TraceLotEntity create(
            UUID tenantId, String lotCode, UUID batchId,
            UUID cycleId, UUID fieldId, UUID qualityAssessmentId,
            String grade, BigDecimal quantity, String unit,
            LocalDate expiryDate, String qrToken, String snapshot) {
        TraceLotEntity value = new TraceLotEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.lotCode = lotCode;
        value.harvestBatchId = batchId;
        value.cropCycleId = cycleId;
        value.fieldId = fieldId;
        value.qualityAssessmentId = qualityAssessmentId;
        value.qualityGrade = grade;
        value.quantity = quantity;
        value.quantityUnit = unit;
        value.expiryDate = expiryDate;
        value.status = grade == null ? "CREATED" : "GRADED";
        value.qrToken = qrToken;
        value.traceSnapshot = snapshot;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void markPacked() {
        status = "PACKED";
        packedAt = Instant.now();
        updatedAt = packedAt;
    }

    public void recall() {
        status = "RECALLED";
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String lotCode() { return lotCode; }
    public UUID harvestBatchId() { return harvestBatchId; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID fieldId() { return fieldId; }
    public String qualityGrade() { return qualityGrade; }
    public BigDecimal quantity() { return quantity; }
    public String quantityUnit() { return quantityUnit; }
    public String status() { return status; }
    public String qrToken() { return qrToken; }
    public String traceSnapshot() { return traceSnapshot; }
    public long version() { return version; }
}
