package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "compliance_profile", schema = "compliance")
public class ComplianceProfileEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID farmerId;
    private UUID farmId;
    @Column(nullable = false) private String profileName;
    @Column(nullable = false) private String profileType;
    private String geographyCode;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected ComplianceProfileEntity() {}

    public static ComplianceProfileEntity create(
            UUID tenantId, UUID farmerId, UUID farmId,
            String profileName, String profileType, String geographyCode) {
        ComplianceProfileEntity value = new ComplianceProfileEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.farmId = farmId;
        value.profileName = profileName;
        value.profileType = profileType;
        value.geographyCode = geographyCode;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public UUID farmId() { return farmId; }
    public String profileType() { return profileType; }
}
