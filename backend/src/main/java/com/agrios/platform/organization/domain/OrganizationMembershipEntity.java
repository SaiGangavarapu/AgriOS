package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "organization_membership", schema = "organization")
public class OrganizationMembershipEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID organizationId;
    @Column(nullable = false) private UUID farmerId;
    @Column(nullable = false) private String membershipNumber;
    @Column(nullable = false) private String membershipType;
    @Column(nullable = false) private LocalDate joinedOn;
    @Column(nullable = false) private BigDecimal shareUnits;
    @Column(nullable = false) private BigDecimal shareValue;
    @Column(nullable = false) private String status;
    private Instant approvedAt;
    private UUID approvedBy;
    @Column(nullable = false) private Instant createdAt;

    protected OrganizationMembershipEntity() {}

    public static OrganizationMembershipEntity create(
            UUID tenantId, UUID organizationId, UUID farmerId,
            String membershipNumber, String membershipType,
            LocalDate joinedOn, BigDecimal shareUnits,
            BigDecimal shareValue) {
        OrganizationMembershipEntity value = new OrganizationMembershipEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationId = organizationId;
        value.farmerId = farmerId;
        value.membershipNumber = membershipNumber;
        value.membershipType = membershipType;
        value.joinedOn = joinedOn;
        value.shareUnits = shareUnits == null ? BigDecimal.ZERO : shareUnits;
        value.shareValue = shareValue == null ? BigDecimal.ZERO : shareValue;
        value.status = "PENDING";
        value.createdAt = Instant.now();
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("PENDING")) throw new IllegalStateException("Membership is not pending.");
        status = "ACTIVE";
        approvedAt = Instant.now();
        approvedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID organizationId() { return organizationId; }
    public UUID farmerId() { return farmerId; }
    public String status() { return status; }
}
