package com.agrios.platform.market.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.market.application.MarketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MarketController {
    private final MarketService service;
    private final TenantContextResolver tenants;

    public MarketController(MarketService service,
                            TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/market/sources")
    Map<String, UUID> source(
            @Valid @RequestBody MarketDtos.SourceRequest body,
            HttpServletRequest request) {
        return Map.of("sourceId", service.createSource(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/market/locations")
    Map<String, UUID> location(
            @Valid @RequestBody MarketDtos.LocationRequest body,
            HttpServletRequest request) {
        return Map.of("marketLocationId", service.createLocation(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/integration/market/prices")
    Map<String, UUID> price(
            @Valid @RequestBody MarketDtos.PriceObservationRequest body,
            HttpServletRequest request) {
        return Map.of("priceObservationId", service.ingestPrice(
                tenants.resolve(request).tenantId(), body));
    }

    @GetMapping("/market/crops/{cropId}/price-summary")
    MarketDtos.PriceSummaryResponse priceSummary(
            @PathVariable UUID cropId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            HttpServletRequest request) {
        return service.priceSummary(
                tenants.resolve(request).tenantId(), cropId, start, end);
    }

    @PostMapping("/sales/plans")
    MarketDtos.SalesPlanResponse salesPlan(
            @Valid @RequestBody MarketDtos.SalesPlanRequest body,
            HttpServletRequest request) {
        return service.createSalesPlan(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/sales/plans/{planId}/approve")
    void approvePlan(
            @PathVariable UUID planId,
            HttpServletRequest request) {
        service.approveSalesPlan(
                tenants.resolve(request).tenantId(), actorId(), planId);
    }

    @PostMapping("/sales/buyers")
    Map<String, UUID> buyer(
            @Valid @RequestBody MarketDtos.BuyerRequest body,
            HttpServletRequest request) {
        return Map.of("buyerId", service.createBuyer(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/sales/plans/{planId}/offers")
    Map<String, UUID> offer(
            @PathVariable UUID planId,
            @Valid @RequestBody MarketDtos.OfferRequest body,
            HttpServletRequest request) {
        return Map.of("offerId", service.createOffer(
                tenants.resolve(request).tenantId(), planId, body));
    }

    @PostMapping("/sales/offers/{offerId}/accept")
    void acceptOffer(
            @PathVariable UUID offerId,
            HttpServletRequest request) {
        service.acceptOffer(
                tenants.resolve(request).tenantId(), offerId);
    }

    @PostMapping("/marketplace/connectors")
    Map<String, UUID> connector(
            @Valid @RequestBody MarketDtos.ConnectorRequest body,
            HttpServletRequest request) {
        return Map.of("connectorId", service.createConnector(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/marketplace/listings")
    Map<String, UUID> listing(
            @Valid @RequestBody MarketDtos.ListingRequest body,
            HttpServletRequest request) {
        return Map.of("listingId", service.createListing(
                tenants.resolve(request).tenantId(), body));
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
