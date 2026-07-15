package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resource_booking", schema = "organization")
public class ResourceBookingEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID organizationId;
    @Column(nullable = false) private UUID resourceId;
    @Column(nullable = false) private UUID membershipId;
    @Column(nullable = false) private Instant bookingStart;
    @Column(nullable = false) private Instant bookingEnd;
    @Column(nullable = false) private String purpose;
    @Column(nullable = false) private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    @Column(nullable = false) private String status;
    private Instant approvedAt;
    private UUID approvedBy;
    @Column(nullable = false) private Instant createdAt;

    protected ResourceBookingEntity() {}

    public static ResourceBookingEntity create(
            UUID tenantId, UUID organizationId, UUID resourceId,
            UUID membershipId, Instant start, Instant end,
            String purpose, BigDecimal estimatedCost) {
        ResourceBookingEntity value = new ResourceBookingEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationId = organizationId;
        value.resourceId = resourceId;
        value.membershipId = membershipId;
        value.bookingStart = start;
        value.bookingEnd = end;
        value.purpose = purpose;
        value.estimatedCost = estimatedCost == null ? BigDecimal.ZERO : estimatedCost;
        value.status = "REQUESTED";
        value.createdAt = Instant.now();
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("REQUESTED")) throw new IllegalStateException("Booking is not requested.");
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
    }

    public UUID id() { return id; }
}
