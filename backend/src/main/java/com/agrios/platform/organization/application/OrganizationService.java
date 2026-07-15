package com.agrios.platform.organization.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.organization.api.OrganizationDtos;
import com.agrios.platform.organization.domain.*;
import com.agrios.platform.traceability.domain.TraceLotRepository;
import java.math.*;
import java.time.Duration;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {
    private final FarmerOrganizationRepository organizations;
    private final OrganizationMembershipRepository memberships;
    private final SharedResourceRepository resources;
    private final ResourceBookingRepository bookings;
    private final ProcurementRequestRepository procurementRequests;
    private final CollectiveCollectionLotRepository collectionLots;
    private final CollectiveContributionRepository contributions;
    private final FarmerRepository farmers;
    private final TraceLotRepository traceLots;

    public OrganizationService(
            FarmerOrganizationRepository organizations,
            OrganizationMembershipRepository memberships,
            SharedResourceRepository resources,
            ResourceBookingRepository bookings,
            ProcurementRequestRepository procurementRequests,
            CollectiveCollectionLotRepository collectionLots,
            CollectiveContributionRepository contributions,
            FarmerRepository farmers,
            TraceLotRepository traceLots) {
        this.organizations = organizations;
        this.memberships = memberships;
        this.resources = resources;
        this.bookings = bookings;
        this.procurementRequests = procurementRequests;
        this.collectionLots = collectionLots;
        this.contributions = contributions;
        this.farmers = farmers;
        this.traceLots = traceLots;
    }

    @Transactional
    public OrganizationDtos.OrganizationResponse createOrganization(
            UUID tenantId, OrganizationDtos.OrganizationRequest request) {
        FarmerOrganizationEntity value = FarmerOrganizationEntity.create(
                tenantId, request.organizationCode(),
                request.organizationName(), request.organizationType(),
                request.registrationNumber(), request.registrationAuthority(),
                request.registrationDate(), request.primaryGeographyCode());
        return OrganizationDtos.OrganizationResponse.from(
                organizations.save(value));
    }

    @Transactional
    public UUID addMembership(
            UUID tenantId, UUID organizationId,
            OrganizationDtos.MembershipRequest request) {
        requireOrganization(tenantId, organizationId);
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        return memberships.save(OrganizationMembershipEntity.create(
                tenantId, organizationId, request.farmerId(),
                request.membershipNumber(), request.membershipType(),
                request.joinedOn(), request.shareUnits(),
                request.shareValue())).id();
    }

    @Transactional
    public void approveMembership(
            UUID tenantId, UUID actorId, UUID membershipId) {
        memberships.findByIdAndTenantId(membershipId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MEMBERSHIP_NOT_FOUND", "Membership not found."))
                .approve(actorId);
    }

    @Transactional
    public UUID createSharedResource(
            UUID tenantId, UUID organizationId,
            OrganizationDtos.SharedResourceRequest request) {
        requireOrganization(tenantId, organizationId);
        return resources.save(SharedResourceEntity.create(
                tenantId, organizationId, request.resourceCode(),
                request.resourceName(), request.resourceType(),
                request.capacity(), request.capacityUnit(),
                request.hourlyRate(), request.dailyRate())).id();
    }

    @Transactional
    public UUID bookResource(
            UUID tenantId, UUID resourceId,
            OrganizationDtos.BookingRequest request) {
        SharedResourceEntity resource = resources
                .findByIdAndTenantId(resourceId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SHARED_RESOURCE_NOT_FOUND", "Shared resource not found."));
        OrganizationMembershipEntity membership = memberships
                .findByIdAndTenantId(request.membershipId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MEMBERSHIP_NOT_FOUND", "Membership not found."));
        if (!membership.organizationId().equals(resource.organizationId())) {
            throw new ConflictException(
                    "RESOURCE_MEMBERSHIP_ORGANIZATION_MISMATCH",
                    "Membership does not belong to the resource organization.");
        }
        if (!"ACTIVE".equals(membership.status())) {
            throw new BusinessException(
                    "MEMBERSHIP_NOT_ACTIVE",
                    "Only active members can book resources.", 422);
        }
        boolean overlapping = bookings
                .existsByResourceIdAndStatusInAndBookingStartLessThanAndBookingEndGreaterThan(
                        resourceId, Set.of("APPROVED","IN_PROGRESS"),
                        request.bookingEnd(), request.bookingStart());
        if (overlapping) {
            throw new ConflictException(
                    "RESOURCE_BOOKING_CONFLICT",
                    "Resource is already booked for the requested time.");
        }

        BigDecimal estimated = BigDecimal.ZERO;
        if (resource.hourlyRate() != null) {
            long minutes = Duration.between(
                    request.bookingStart(), request.bookingEnd()).toMinutes();
            estimated = resource.hourlyRate()
                    .multiply(BigDecimal.valueOf(minutes))
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        }

        return bookings.save(ResourceBookingEntity.create(
                tenantId, resource.organizationId(),
                resource.id(), membership.id(),
                request.bookingStart(), request.bookingEnd(),
                request.purpose(), estimated)).id();
    }

    @Transactional
    public UUID createProcurementRequest(
            UUID tenantId, UUID organizationId,
            OrganizationDtos.ProcurementRequest request) {
        requireOrganization(tenantId, organizationId);
        return procurementRequests.save(ProcurementRequestEntity.create(
                tenantId, organizationId, request.requestCode(),
                request.itemCategory(), request.itemName(),
                request.targetQuantity(), request.quantityUnit(),
                request.requiredBy())).id();
    }

    @Transactional
    public UUID createCollectionLot(
            UUID tenantId, UUID organizationId,
            OrganizationDtos.CollectionLotRequest request) {
        requireOrganization(tenantId, organizationId);
        return collectionLots.save(CollectiveCollectionLotEntity.create(
                tenantId, organizationId, request.collectionCode(),
                request.cropId(), request.varietyId(),
                request.gradeCode(), request.quantityUnit())).id();
    }

    @Transactional
    public UUID contribute(
            UUID tenantId, UUID collectionLotId,
            OrganizationDtos.ContributionRequest request) {
        CollectiveCollectionLotEntity collection = collectionLots
                .findByIdAndTenantId(collectionLotId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "COLLECTION_LOT_NOT_FOUND", "Collection lot not found."));
        OrganizationMembershipEntity membership = memberships
                .findByIdAndTenantId(request.membershipId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MEMBERSHIP_NOT_FOUND", "Membership not found."));
        if (!"ACTIVE".equals(membership.status())) {
            throw new BusinessException(
                    "MEMBERSHIP_NOT_ACTIVE",
                    "Only active members can contribute produce.", 422);
        }
        if (request.traceLotId() != null) {
            traceLots.findByIdAndTenantId(request.traceLotId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "TRACE_LOT_NOT_FOUND", "Traceability lot not found."));
        }

        CollectiveContributionEntity contribution =
                contributions.save(CollectiveContributionEntity.create(
                        collection.id(), membership.id(),
                        request.traceLotId(),
                        request.contributedQuantity(),
                        request.qualityGrade()));
        collection.addQuantity(request.contributedQuantity());
        return contribution.id();
    }

    private FarmerOrganizationEntity requireOrganization(
            UUID tenantId, UUID organizationId) {
        return organizations.findByIdAndTenantId(organizationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_ORGANIZATION_NOT_FOUND",
                        "Farmer organization not found."));
    }
}
