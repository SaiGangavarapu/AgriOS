package com.agrios.platform.farmer.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "farmer_verification", schema = "farmer")
public class FarmerVerificationEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID farmerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationLevel verificationLevel;
    @Column(nullable = false)
    private String evidenceType;
    private String evidenceReference;
    private UUID verifiedBy;
    @Column(nullable = false)
    private Instant verifiedAt;
    private Instant expiresAt;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Instant createdAt;

    protected FarmerVerificationEntity() {}

    public static FarmerVerificationEntity create(UUID farmerId, VerificationLevel level,
                                                   String evidenceType, String evidenceReference,
                                                   UUID actorId, Instant expiresAt) {
        FarmerVerificationEntity value = new FarmerVerificationEntity();
        value.id = UUID.randomUUID();
        value.farmerId = farmerId;
        value.verificationLevel = level;
        value.evidenceType = evidenceType;
        value.evidenceReference = evidenceReference;
        value.verifiedBy = actorId;
        value.verifiedAt = Instant.now();
        value.expiresAt = expiresAt;
        value.status = "VALID";
        value.createdAt = Instant.now();
        return value;
    }
}
