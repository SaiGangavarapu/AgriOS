package com.agrios.platform.market.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.CropCycleRepository;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.market.api.MarketDtos;
import com.agrios.platform.market.domain.*;
import com.agrios.platform.traceability.domain.TraceLotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketService {
    private final MarketSourceRepository sources;
    private final MarketLocationRepository locations;
    private final CommodityPriceObservationRepository prices;
    private final SalesPlanRepository salesPlans;
    private final BuyerProfileRepository buyers;
    private final BuyerOfferRepository offers;
    private final MarketplaceConnectorRepository connectors;
    private final MarketplaceListingRepository listings;
    private final FarmerRepository farmers;
    private final CropCycleRepository cycles;
    private final TraceLotRepository lots;
    private final ObjectMapper mapper;

    public MarketService(
            MarketSourceRepository sources,
            MarketLocationRepository locations,
            CommodityPriceObservationRepository prices,
            SalesPlanRepository salesPlans,
            BuyerProfileRepository buyers,
            BuyerOfferRepository offers,
            MarketplaceConnectorRepository connectors,
            MarketplaceListingRepository listings,
            FarmerRepository farmers,
            CropCycleRepository cycles,
            TraceLotRepository lots,
            ObjectMapper mapper) {
        this.sources = sources;
        this.locations = locations;
        this.prices = prices;
        this.salesPlans = salesPlans;
        this.buyers = buyers;
        this.offers = offers;
        this.connectors = connectors;
        this.listings = listings;
        this.farmers = farmers;
        this.cycles = cycles;
        this.lots = lots;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createSource(UUID tenantId, MarketDtos.SourceRequest request) {
        return sources.save(MarketSourceEntity.create(
                tenantId, request.sourceCode(), request.sourceName(),
                request.sourceType(), request.baseUrl(),
                request.licenseReference(), request.priority())).id();
    }

    @Transactional
    public UUID createLocation(UUID tenantId, MarketDtos.LocationRequest request) {
        return locations.save(MarketLocationEntity.create(
                tenantId, request.locationCode(), request.locationName(),
                request.locationType(), request.districtCode(),
                request.stateCode(), request.countryCode(),
                request.latitude(), request.longitude())).id();
    }

    @Transactional
    public UUID ingestPrice(UUID tenantId, MarketDtos.PriceObservationRequest request) {
        sources.findByIdAndTenantId(request.sourceId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MARKET_SOURCE_NOT_FOUND", "Market source not found."));
        if (request.marketLocationId() != null) {
            locations.findByIdAndTenantId(request.marketLocationId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "MARKET_LOCATION_NOT_FOUND", "Market location not found."));
        }
        return prices.save(CommodityPriceObservationEntity.create(
                tenantId, request.sourceId(), request.marketLocationId(),
                request.cropId(), request.varietyId(), request.gradeCode(),
                request.observedDate(), request.minPrice(), request.maxPrice(),
                request.modalPrice(), request.currencyCode(),
                request.quantityUnit(), request.arrivalsQuantity(),
                request.sourceQuality(),
                json(request.rawPayload() == null ? Map.of() : request.rawPayload()))).id();
    }

    @Transactional(readOnly = true)
    public MarketDtos.PriceSummaryResponse priceSummary(
            UUID tenantId, UUID cropId, LocalDate start, LocalDate end) {
        List<CommodityPriceObservationEntity> values =
                prices.findByTenantIdAndCropIdAndObservedDateBetweenOrderByObservedDateDesc(
                        tenantId, cropId, start, end);
        if (values.isEmpty()) {
            throw new ResourceNotFoundException(
                    "PRICE_DATA_NOT_FOUND", "Price data not found.");
        }
        BigDecimal latest = values.getFirst().modalPrice();
        BigDecimal min = values.stream().map(CommodityPriceObservationEntity::modalPrice)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = values.stream().map(CommodityPriceObservationEntity::modalPrice)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal avg = values.stream().map(CommodityPriceObservationEntity::modalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), 4, RoundingMode.HALF_UP);
        return new MarketDtos.PriceSummaryResponse(
                latest, min, max, avg,
                values.getFirst().quantityUnit(), values.size());
    }

    @Transactional
    public MarketDtos.SalesPlanResponse createSalesPlan(
            UUID tenantId, MarketDtos.SalesPlanRequest request) {
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        if (request.traceLotId() != null) {
            lots.findByIdAndTenantId(request.traceLotId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "TRACE_LOT_NOT_FOUND", "Traceability lot not found."));
        }
        SalesPlanEntity value = SalesPlanEntity.create(
                tenantId, request.farmerId(), request.cropCycleId(),
                request.traceLotId(), request.planName(),
                request.availableQuantity(), request.quantityUnit(),
                request.minimumAcceptablePrice(), request.targetPrice(),
                request.currencyCode(), request.salesWindowStart(),
                request.salesWindowEnd(), request.strategy(),
                json(request.recommendationSnapshot() == null
                        ? Map.of() : request.recommendationSnapshot()));
        return MarketDtos.SalesPlanResponse.from(salesPlans.save(value));
    }

    @Transactional
    public void approveSalesPlan(UUID tenantId, UUID actorId, UUID planId) {
        SalesPlanEntity plan = requireSalesPlan(tenantId, planId);
        plan.approve(actorId);
        plan.activate();
    }

    @Transactional
    public UUID createBuyer(UUID tenantId, MarketDtos.BuyerRequest request) {
        return buyers.save(BuyerProfileEntity.create(
                tenantId, request.buyerCode(), request.buyerName(),
                request.buyerType(), request.mobile(), request.email(),
                request.gstin(),
                json(request.address() == null ? Map.of() : request.address()),
                request.rating(), request.paymentReliabilityScore())).id();
    }

    @Transactional
    public UUID createOffer(UUID tenantId, UUID planId,
                            MarketDtos.OfferRequest request) {
        SalesPlanEntity plan = requireSalesPlan(tenantId, planId);
        if (!"ACTIVE".equals(plan.status())) {
            throw new BusinessException("SALES_PLAN_NOT_ACTIVE",
                    "Sales plan must be active before receiving offers.", 422);
        }
        buyers.findByIdAndTenantId(request.buyerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BUYER_NOT_FOUND", "Buyer not found."));
        if (request.offeredQuantity().compareTo(plan.availableQuantity()) > 0) {
            throw new BusinessException("OFFER_QUANTITY_EXCEEDS_AVAILABLE",
                    "Offer quantity exceeds available quantity.", 422);
        }
        return offers.save(BuyerOfferEntity.create(
                tenantId, plan.id(), request.buyerId(),
                request.offeredQuantity(), request.quantityUnit(),
                request.offeredPrice(), request.currencyCode(),
                request.pickupTerms(), request.paymentTerms(),
                request.validUntil(), request.notes())).id();
    }

    @Transactional
    public void acceptOffer(UUID tenantId, UUID offerId) {
        BuyerOfferEntity offer = offers.findByIdAndTenantId(offerId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BUYER_OFFER_NOT_FOUND", "Buyer offer not found."));
        SalesPlanEntity plan = requireSalesPlan(tenantId, offer.salesPlanId());
        if (plan.minimumAcceptablePrice() != null &&
                offer.offeredPrice().compareTo(plan.minimumAcceptablePrice()) < 0) {
            throw new BusinessException("OFFER_BELOW_MINIMUM_PRICE",
                    "Offer is below the minimum acceptable price.", 422);
        }
        offer.accept();
    }

    @Transactional
    public UUID createConnector(UUID tenantId,
                                MarketDtos.ConnectorRequest request) {
        return connectors.save(MarketplaceConnectorEntity.create(
                tenantId, request.connectorCode(), request.connectorName(),
                request.connectorType(), request.endpointReference(),
                request.authenticationReference())).id();
    }

    @Transactional
    public UUID createListing(UUID tenantId, MarketDtos.ListingRequest request) {
        if (listings.existsByTenantIdAndIdempotencyKey(
                tenantId, request.idempotencyKey())) {
            throw new ConflictException("MARKETPLACE_LISTING_DUPLICATE",
                    "Marketplace listing idempotency key already exists.");
        }
        MarketplaceConnectorEntity connector =
                connectors.findByIdAndTenantId(request.connectorId(), tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "MARKETPLACE_CONNECTOR_NOT_FOUND",
                                "Marketplace connector not found."));
        if (!"ACTIVE".equals(connector.status())) {
            throw new BusinessException("MARKETPLACE_CONNECTOR_NOT_ACTIVE",
                    "Marketplace connector is not active.", 422);
        }
        SalesPlanEntity plan = requireSalesPlan(tenantId, request.salesPlanId());
        if (!"ACTIVE".equals(plan.status())) {
            throw new BusinessException("SALES_PLAN_NOT_ACTIVE",
                    "Sales plan must be active before listing.", 422);
        }
        if (request.listedQuantity().compareTo(plan.availableQuantity()) > 0) {
            throw new BusinessException("LISTING_QUANTITY_EXCEEDS_AVAILABLE",
                    "Listing quantity exceeds sales-plan availability.", 422);
        }
        MarketplaceListingEntity listing = MarketplaceListingEntity.create(
                tenantId, connector.id(), plan.id(), request.traceLotId(),
                request.title(), request.description(),
                request.listedQuantity(), request.quantityUnit(),
                request.listPrice(), request.currencyCode(),
                request.fulfillmentMode(), request.idempotencyKey());
        listing.publish("LOCAL-" + UUID.randomUUID());
        return listings.save(listing).id();
    }

    private SalesPlanEntity requireSalesPlan(UUID tenantId, UUID planId) {
        return salesPlans.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SALES_PLAN_NOT_FOUND", "Sales plan not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(
                    "JSON_SERIALIZATION_FAILED",
                    "Unable to serialize market data.", 500);
        }
    }
}
