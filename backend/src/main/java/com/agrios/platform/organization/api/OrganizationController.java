package com.agrios.platform.organization.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.organization.application.OrganizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrganizationController {
    private final OrganizationService service;
    private final TenantContextResolver tenants;

    public OrganizationController(
            OrganizationService service,
            TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/organizations")
    OrganizationDtos.OrganizationResponse organization(
            @Valid @RequestBody OrganizationDtos.OrganizationRequest body,
            HttpServletRequest request) {
        return service.createOrganization(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/organizations/{organizationId}/memberships")
    Map<String, UUID> membership(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationDtos.MembershipRequest body,
            HttpServletRequest request) {
        return Map.of("membershipId", service.addMembership(
                tenants.resolve(request).tenantId(),
                organizationId, body));
    }

    @PostMapping("/organizations/memberships/{membershipId}/approve")
    void approveMembership(
            @PathVariable UUID membershipId,
            HttpServletRequest request) {
        service.approveMembership(
                tenants.resolve(request).tenantId(),
                actorId(), membershipId);
    }

    @PostMapping("/organizations/{organizationId}/resources")
    Map<String, UUID> resource(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationDtos.SharedResourceRequest body,
            HttpServletRequest request) {
        return Map.of("resourceId", service.createSharedResource(
                tenants.resolve(request).tenantId(),
                organizationId, body));
    }

    @PostMapping("/organizations/resources/{resourceId}/bookings")
    Map<String, UUID> booking(
            @PathVariable UUID resourceId,
            @Valid @RequestBody OrganizationDtos.BookingRequest body,
            HttpServletRequest request) {
        return Map.of("bookingId", service.bookResource(
                tenants.resolve(request).tenantId(),
                resourceId, body));
    }

    @PostMapping("/organizations/{organizationId}/procurement-requests")
    Map<String, UUID> procurement(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationDtos.ProcurementRequest body,
            HttpServletRequest request) {
        return Map.of("procurementRequestId",
                service.createProcurementRequest(
                        tenants.resolve(request).tenantId(),
                        organizationId, body));
    }

    @PostMapping("/organizations/{organizationId}/collection-lots")
    Map<String, UUID> collectionLot(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationDtos.CollectionLotRequest body,
            HttpServletRequest request) {
        return Map.of("collectionLotId",
                service.createCollectionLot(
                        tenants.resolve(request).tenantId(),
                        organizationId, body));
    }

    @PostMapping("/organizations/collection-lots/{collectionLotId}/contributions")
    Map<String, UUID> contribution(
            @PathVariable UUID collectionLotId,
            @Valid @RequestBody OrganizationDtos.ContributionRequest body,
            HttpServletRequest request) {
        return Map.of("contributionId", service.contribute(
                tenants.resolve(request).tenantId(),
                collectionLotId, body));
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes(
                "development-actor".getBytes());
    }
}
