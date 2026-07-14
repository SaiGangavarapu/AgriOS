package com.agrios.platform.soilwater.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sample", schema = "soilwater")
public class SampleEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID fieldId;
    private UUID waterSourceId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SampleType sampleType;
    @Column(nullable = false, length = 100) private String sampleCode;
    @Column(nullable = false) private Instant collectedAt;
    private UUID collectedBy;
    private BigDecimal collectionDepthCm;
    @Column(nullable = false, length = 80) private String collectionMethod;
    @Column(columnDefinition = "text") private String recentInputNotes;
    @Column(nullable = false, length = 40) private String status;
    @Column(columnDefinition = "text") private String rejectionReason;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected SampleEntity() {}

    public static SampleEntity soil(UUID tenantId, UUID fieldId, String code,
                                    Instant collectedAt, UUID actorId,
                                    BigDecimal depth, String method, String notes) {
        return create(tenantId, fieldId, null, SampleType.SOIL, code,
                collectedAt, actorId, depth, method, notes);
    }

    public static SampleEntity water(UUID tenantId, UUID waterSourceId, String code,
                                     Instant collectedAt, UUID actorId,
                                     String method, String notes) {
        return create(tenantId, null, waterSourceId, SampleType.WATER, code,
                collectedAt, actorId, null, method, notes);
    }

    private static SampleEntity create(UUID tenantId, UUID fieldId, UUID waterSourceId,
                                       SampleType type, String code, Instant collectedAt,
                                       UUID actorId, BigDecimal depth, String method, String notes) {
        SampleEntity value = new SampleEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.waterSourceId = waterSourceId;
        value.sampleType = type;
        value.sampleCode = code.trim();
        value.collectedAt = collectedAt;
        value.collectedBy = actorId;
        value.collectionDepthCm = depth;
        value.collectionMethod = method;
        value.recentInputNotes = notes;
        value.status = "COLLECTED";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void markPublished(UUID actorId) {
        status = "PUBLISHED";
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID fieldId() { return fieldId; }
    public UUID waterSourceId() { return waterSourceId; }
    public SampleType sampleType() { return sampleType; }
    public String sampleCode() { return sampleCode; }
    public Instant collectedAt() { return collectedAt; }
    public BigDecimal collectionDepthCm() { return collectionDepthCm; }
    public String collectionMethod() { return collectionMethod; }
    public String recentInputNotes() { return recentInputNotes; }
    public String status() { return status; }
    public long version() { return version; }
}
