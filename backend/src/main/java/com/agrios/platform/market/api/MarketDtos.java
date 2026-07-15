package com.agrios.platform.market.api;

import com.agrios.platform.market.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class MarketDtos {
    private MarketDtos() {}

    public record SourceRequest(
            @NotBlank String sourceCode,
            @NotBlank String sourceName,
            @NotBlank String sourceType,
            String baseUrl,
            String licenseReference,
            @PositiveOrZero int priority) {}

    public record LocationRequest(
            @NotBlank String locationCode,
            @NotBlank String locationName,
            @NotBlank String locationType,
            String districtCode,
            String stateCode,
            @NotBlank String countryCode,
            BigDecimal latitude,
            BigDecimal longitude) {}

    public record PriceObservationRequest(
            @NotNull UUID sourceId,
            UUID marketLocationId,
            @NotNull UUID cropId,
            UUID varietyId,
            String gradeCode,
            @NotNull LocalDate observedDate,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            @NotNull @DecimalMin("0.0") BigDecimal modalPrice,
            @NotBlank String currencyCode,
            @NotBlank String quantityUnit,
            BigDecimal arrivalsQuantity,
            @NotBlank String sourceQuality,
            Map<String,Object> rawPayload) {}

    public record SalesPlanRequest(
            @NotNull UUID farmerId,
            @NotNull UUID cropCycleId,
            UUID traceLotId,
            @NotBlank String planName,
            @NotNull @DecimalMin("0.0001") BigDecimal availableQuantity,
            @NotBlank String quantityUnit,
            BigDecimal minimumAcceptablePrice,
            BigDecimal targetPrice,
            @NotBlank String currencyCode,
            @NotNull LocalDate salesWindowStart,
            @NotNull LocalDate salesWindowEnd,
            @NotBlank String strategy,
            Map<String,Object> recommendationSnapshot) {}

    public record BuyerRequest(
            @NotBlank String buyerCode,
            @NotBlank String buyerName,
            @NotBlank String buyerType,
            String mobile,
            String email,
            String gstin,
            Map<String,Object> address,
            BigDecimal rating,
            BigDecimal paymentReliabilityScore) {}

    public record OfferRequest(
            @NotNull UUID buyerId,
            @NotNull @DecimalMin("0.0001") BigDecimal offeredQuantity,
            @NotBlank String quantityUnit,
            @NotNull @DecimalMin("0.0") BigDecimal offeredPrice,
            @NotBlank String currencyCode,
            String pickupTerms,
            String paymentTerms,
            Instant validUntil,
            String notes) {}

    public record ConnectorRequest(
            @NotBlank String connectorCode,
            @NotBlank String connectorName,
            @NotBlank String connectorType,
            String endpointReference,
            String authenticationReference) {}

    public record ListingRequest(
            @NotNull UUID connectorId,
            @NotNull UUID salesPlanId,
            UUID traceLotId,
            @NotBlank String title,
            String description,
            @NotNull @DecimalMin("0.0001") BigDecimal listedQuantity,
            @NotBlank String quantityUnit,
            @NotNull @DecimalMin("0.0") BigDecimal listPrice,
            @NotBlank String currencyCode,
            @NotBlank String fulfillmentMode,
            @NotBlank String idempotencyKey) {}

    public record PriceSummaryResponse(
            BigDecimal latestModalPrice,
            BigDecimal minimumObservedPrice,
            BigDecimal maximumObservedPrice,
            BigDecimal averageModalPrice,
            String quantityUnit,
            int observationCount) {}

    public record SalesPlanResponse(
            UUID id, UUID farmerId, UUID cropCycleId, UUID traceLotId,
            BigDecimal availableQuantity, String quantityUnit,
            BigDecimal minimumAcceptablePrice, BigDecimal targetPrice,
            String currencyCode, String strategy, String status, long version) {
        public static SalesPlanResponse from(SalesPlanEntity value) {
            return new SalesPlanResponse(value.id(), value.farmerId(),
                    value.cropCycleId(), value.traceLotId(),
                    value.availableQuantity(), value.quantityUnit(),
                    value.minimumAcceptablePrice(), value.targetPrice(),
                    value.currencyCode(), value.strategy(),
                    value.status(), value.version());
        }
    }
}
