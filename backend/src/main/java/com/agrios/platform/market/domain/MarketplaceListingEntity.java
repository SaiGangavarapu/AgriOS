package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "marketplace_listing", schema = "market")
public class MarketplaceListingEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID connectorId;
    @Column(nullable = false) private UUID salesPlanId;
    private UUID traceLotId;
    private String externalListingId;
    @Column(nullable = false) private String title;
    @Column(columnDefinition = "text") private String description;
    @Column(nullable = false) private BigDecimal listedQuantity;
    @Column(nullable = false) private BigDecimal availableQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private BigDecimal listPrice;
    @Column(nullable = false) private String currencyCode;
    @Column(nullable = false) private String fulfillmentMode;
    @Column(nullable = false) private String status;
    private Instant publishedAt;
    private Instant lastSyncedAt;
    @Column(columnDefinition = "text") private String syncError;
    @Column(nullable = false) private String idempotencyKey;
    @Column(nullable = false) private Instant createdAt;

    protected MarketplaceListingEntity() {}

    public static MarketplaceListingEntity create(
            UUID tenantId, UUID connectorId, UUID salesPlanId,
            UUID traceLotId, String title, String description,
            BigDecimal quantity, String unit, BigDecimal price,
            String currency, String fulfillmentMode,
            String idempotencyKey) {
        MarketplaceListingEntity value = new MarketplaceListingEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.connectorId = connectorId;
        value.salesPlanId = salesPlanId;
        value.traceLotId = traceLotId;
        value.title = title;
        value.description = description;
        value.listedQuantity = quantity;
        value.availableQuantity = quantity;
        value.quantityUnit = unit;
        value.listPrice = price;
        value.currencyCode = currency;
        value.fulfillmentMode = fulfillmentMode;
        value.status = "DRAFT";
        value.idempotencyKey = idempotencyKey;
        value.createdAt = Instant.now();
        return value;
    }

    public void publish(String externalListingId) {
        status = "ACTIVE";
        this.externalListingId = externalListingId;
        publishedAt = Instant.now();
        lastSyncedAt = publishedAt;
    }

    public UUID id() { return id; }
    public String status() { return status; }
}
