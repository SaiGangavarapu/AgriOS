package com.agrios.platform.farm.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "water_source", schema = "farm")
public class WaterSourceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmId;
    @Column(nullable = false) private String sourceType;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String reliability;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected WaterSourceEntity() {}

    public static WaterSourceEntity create(UUID tenantId, UUID farmId, String sourceType,
                                           String name, String reliability, UUID actorId) {
        WaterSourceEntity value = new WaterSourceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmId = farmId;
        value.sourceType = sourceType;
        value.name = name.trim();
        value.reliability = reliability;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public UUID id() { return id; }
    public UUID farmId() { return farmId; }
    public String sourceType() { return sourceType; }
    public String name() { return name; }
    public String reliability() { return reliability; }
    public String status() { return status; }
    public long version() { return version; }
}
