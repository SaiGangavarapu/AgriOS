package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "buyer_offer", schema = "market")
public class BuyerOfferEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID salesPlanId;
    @Column(nullable = false) private UUID buyerId;
    @Column(nullable = false) private Instant offeredAt;
    @Column(nullable = false) private BigDecimal offeredQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private BigDecimal offeredPrice;
    @Column(nullable = false) private String currencyCode;
    private String pickupTerms;
    private String paymentTerms;
    private Instant validUntil;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false) private Instant createdAt;

    protected BuyerOfferEntity() {}

    public static BuyerOfferEntity create(
            UUID tenantId, UUID salesPlanId, UUID buyerId,
            BigDecimal quantity, String unit, BigDecimal price,
            String currency, String pickupTerms, String paymentTerms,
            Instant validUntil, String notes) {
        BuyerOfferEntity value = new BuyerOfferEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.salesPlanId = salesPlanId;
        value.buyerId = buyerId;
        value.offeredAt = Instant.now();
        value.offeredQuantity = quantity;
        value.quantityUnit = unit;
        value.offeredPrice = price;
        value.currencyCode = currency;
        value.pickupTerms = pickupTerms;
        value.paymentTerms = paymentTerms;
        value.validUntil = validUntil;
        value.status = "OPEN";
        value.notes = notes;
        value.createdAt = Instant.now();
        return value;
    }

    public void accept() {
        if (!status.equals("OPEN") && !status.equals("SHORTLISTED")) {
            throw new IllegalStateException("Offer cannot be accepted.");
        }
        status = "ACCEPTED";
    }

    public UUID id() { return id; }
    public UUID salesPlanId() { return salesPlanId; }
    public UUID buyerId() { return buyerId; }
    public BigDecimal offeredQuantity() { return offeredQuantity; }
    public BigDecimal offeredPrice() { return offeredPrice; }
    public String status() { return status; }
}
