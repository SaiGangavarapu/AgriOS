package com.agrios.platform.farmer.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "farmer", schema = "farmer")
public class FarmerEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    private UUID programmeId;
    private UUID linkedUserAccountId;

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(length = 120)
    private String preferredName;

    @Column(nullable = false, length = 10)
    private String preferredLanguage;

    @Column(length = 20)
    private String mobileE164;

    @Column(nullable = false, length = 160)
    private String villageName;

    @Column(length = 160)
    private String districtName;

    @Column(length = 160)
    private String stateName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private VerificationLevel verificationLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private FarmerStatus status;

    private UUID canonicalFarmerId;

    @Column(nullable = false, length = 40)
    private String sourceChannel;

    @Version
    private long version;

    @Column(nullable = false)
    private Instant createdAt;

    private UUID createdBy;

    @Column(nullable = false)
    private Instant updatedAt;

    private UUID updatedBy;

    protected FarmerEntity() {}

    public static FarmerEntity register(UUID tenantId, UUID programmeId, String fullName,
                                        String preferredName, String preferredLanguage,
                                        String mobileE164, String villageName,
                                        String districtName, String stateName,
                                        String sourceChannel, UUID actorId) {
        FarmerEntity farmer = new FarmerEntity();
        farmer.id = UUID.randomUUID();
        farmer.tenantId = tenantId;
        farmer.programmeId = programmeId;
        farmer.fullName = fullName.trim();
        farmer.preferredName = preferredName;
        farmer.preferredLanguage = preferredLanguage;
        farmer.mobileE164 = mobileE164;
        farmer.villageName = villageName.trim();
        farmer.districtName = districtName;
        farmer.stateName = stateName;
        farmer.verificationLevel = VerificationLevel.UNVERIFIED;
        farmer.status = FarmerStatus.REGISTERED;
        farmer.sourceChannel = sourceChannel;
        farmer.createdAt = Instant.now();
        farmer.updatedAt = farmer.createdAt;
        farmer.createdBy = actorId;
        farmer.updatedBy = actorId;
        return farmer;
    }

    public void updateProfile(String fullName, String preferredName, String language,
                              String mobile, String village, String district, String state,
                              UUID actorId) {
        if (status == FarmerStatus.MERGED || status == FarmerStatus.ARCHIVED) {
            throw new IllegalStateException("Farmer cannot be updated in status " + status);
        }
        this.fullName = fullName.trim();
        this.preferredName = preferredName;
        this.preferredLanguage = language;
        this.mobileE164 = mobile;
        this.villageName = village.trim();
        this.districtName = district;
        this.stateName = state;
        this.updatedBy = actorId;
        this.updatedAt = Instant.now();
    }

    public void verify(VerificationLevel level, UUID actorId) {
        if (level.ordinal() < verificationLevel.ordinal()) {
            throw new IllegalArgumentException("Verification level cannot be reduced through verify.");
        }
        verificationLevel = level;
        if (status == FarmerStatus.REGISTERED) status = FarmerStatus.ACTIVE;
        updatedBy = actorId;
        updatedAt = Instant.now();
    }

    public void suspend(UUID actorId) {
        if (status == FarmerStatus.MERGED || status == FarmerStatus.ARCHIVED) {
            throw new IllegalStateException("Farmer cannot be suspended.");
        }
        status = FarmerStatus.SUSPENDED;
        updatedBy = actorId;
        updatedAt = Instant.now();
    }

    public void reactivate(UUID actorId) {
        if (status != FarmerStatus.SUSPENDED) {
            throw new IllegalStateException("Only suspended farmers can be reactivated.");
        }
        status = FarmerStatus.ACTIVE;
        updatedBy = actorId;
        updatedAt = Instant.now();
    }

    public void mergeInto(UUID canonicalId, UUID actorId) {
        if (id.equals(canonicalId)) throw new IllegalArgumentException("Cannot merge farmer into itself.");
        status = FarmerStatus.MERGED;
        canonicalFarmerId = canonicalId;
        updatedBy = actorId;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID programmeId() { return programmeId; }
    public String fullName() { return fullName; }
    public String preferredName() { return preferredName; }
    public String preferredLanguage() { return preferredLanguage; }
    public String mobileE164() { return mobileE164; }
    public String villageName() { return villageName; }
    public String districtName() { return districtName; }
    public String stateName() { return stateName; }
    public VerificationLevel verificationLevel() { return verificationLevel; }
    public FarmerStatus status() { return status; }
    public UUID canonicalFarmerId() { return canonicalFarmerId; }
    public String sourceChannel() { return sourceChannel; }
    public long version() { return version; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
