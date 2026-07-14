package com.agrios.platform.farm.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "field", schema = "farm")
public class FieldEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmId;
    @Column(nullable = false, length = 200) private String name;
    private Integer currentBoundaryVersionNo;
    @Column(precision = 12, scale = 4) private java.math.BigDecimal areaHectares;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private FieldStatus status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected FieldEntity() {}

    public static FieldEntity register(UUID tenantId, UUID farmId, String name, UUID actorId) {
        FieldEntity value = new FieldEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmId = farmId;
        value.name = name.trim();
        value.status = FieldStatus.DRAFT;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void applyBoundary(int versionNo, java.math.BigDecimal area, UUID actorId) {
        currentBoundaryVersionNo = versionNo;
        areaHectares = area;
        status = FieldStatus.BOUNDARY_CAPTURED;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public void markValidated(UUID actorId) {
        status = FieldStatus.VALIDATED;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public void markVerified(UUID actorId) {
        status = FieldStatus.VERIFIED;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID farmId() { return farmId; }
    public String name() { return name; }
    public Integer currentBoundaryVersionNo() { return currentBoundaryVersionNo; }
    public java.math.BigDecimal areaHectares() { return areaHectares; }
    public FieldStatus status() { return status; }
    public long version() { return version; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
