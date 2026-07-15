package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "commodity_price_observation", schema = "market")
public class CommodityPriceObservationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID sourceId;
    private UUID marketLocationId;
    @Column(nullable = false) private UUID cropId;
    private UUID varietyId;
    private String gradeCode;
    @Column(nullable = false) private LocalDate observedDate;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    @Column(nullable = false) private BigDecimal modalPrice;
    @Column(nullable = false) private String currencyCode;
    @Column(nullable = false) private String quantityUnit;
    private BigDecimal arrivalsQuantity;
    @Column(nullable = false) private String sourceQuality;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String rawPayload;
    @Column(nullable = false) private Instant receivedAt;

    protected CommodityPriceObservationEntity() {}

    public static CommodityPriceObservationEntity create(
            UUID tenantId, UUID sourceId, UUID marketLocationId,
            UUID cropId, UUID varietyId, String gradeCode,
            LocalDate observedDate, BigDecimal minPrice,
            BigDecimal maxPrice, BigDecimal modalPrice,
            String currencyCode, String quantityUnit,
            BigDecimal arrivalsQuantity, String sourceQuality,
            String rawPayload) {
        CommodityPriceObservationEntity value = new CommodityPriceObservationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceId = sourceId;
        value.marketLocationId = marketLocationId;
        value.cropId = cropId;
        value.varietyId = varietyId;
        value.gradeCode = gradeCode;
        value.observedDate = observedDate;
        value.minPrice = minPrice;
        value.maxPrice = maxPrice;
        value.modalPrice = modalPrice;
        value.currencyCode = currencyCode;
        value.quantityUnit = quantityUnit;
        value.arrivalsQuantity = arrivalsQuantity;
        value.sourceQuality = sourceQuality;
        value.rawPayload = rawPayload;
        value.receivedAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID marketLocationId() { return marketLocationId; }
    public UUID cropId() { return cropId; }
    public LocalDate observedDate() { return observedDate; }
    public BigDecimal modalPrice() { return modalPrice; }
    public String quantityUnit() { return quantityUnit; }
}
