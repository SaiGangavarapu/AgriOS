package com.agrios.platform.farm.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "farm", schema = "farm")
public class FarmEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID programmeId;
    @Column(nullable = false, length = 200) private String name;
    @Column(nullable = false) private UUID primaryOperatorFarmerId;
    @Column(nullable = false, length = 60) private String farmType;
    @Column(nullable = false, length = 160) private String villageName;
    private String districtName;
    private String stateName;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private FarmStatus status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected FarmEntity() {}

    public static FarmEntity register(UUID tenantId, UUID programmeId, String name,
                                      UUID operatorId, String farmType,
                                      String village, String district, String state,
                                      UUID actorId) {
        FarmEntity value = new FarmEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.programmeId = programmeId;
        value.name = name.trim();
        value.primaryOperatorFarmerId = operatorId;
        value.farmType = farmType;
        value.villageName = village.trim();
        value.districtName = district;
        value.stateName = state;
        value.status = FarmStatus.REGISTERED;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void update(String name, String farmType, String village,
                       String district, String state, UUID actorId) {
        if (status == FarmStatus.ARCHIVED) throw new IllegalStateException("Archived farm is read-only.");
        this.name = name.trim();
        this.farmType = farmType;
        this.villageName = village.trim();
        this.districtName = district;
        this.stateName = state;
        this.updatedAt = Instant.now();
        this.updatedBy = actorId;
    }

    public void verify(UUID actorId) {
        status = FarmStatus.VERIFIED;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID programmeId() { return programmeId; }
    public String name() { return name; }
    public UUID primaryOperatorFarmerId() { return primaryOperatorFarmerId; }
    public String farmType() { return farmType; }
    public String villageName() { return villageName; }
    public String districtName() { return districtName; }
    public String stateName() { return stateName; }
    public FarmStatus status() { return status; }
    public long version() { return version; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
