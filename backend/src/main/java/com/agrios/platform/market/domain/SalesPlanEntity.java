package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sales_plan", schema = "market")
public class SalesPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    @Column(nullable = false) private UUID cropCycleId;
    private UUID traceLotId;
    @Column(nullable = false) private String planName;
    @Column(nullable = false) private BigDecimal availableQuantity;
    @Column(nullable = false) private String quantityUnit;
    private BigDecimal minimumAcceptablePrice;
    private BigDecimal targetPrice;
    @Column(nullable = false) private String currencyCode;
    @Column(nullable = false) private LocalDate salesWindowStart;
    @Column(nullable = false) private LocalDate salesWindowEnd;
    @Column(nullable = false) private String strategy;
    @Column(nullable = false) private String status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String recommendationSnapshot;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected SalesPlanEntity() {}

    public static SalesPlanEntity create(
            UUID tenantId, UUID farmerId, UUID cropCycleId, UUID traceLotId,
            String planName, BigDecimal availableQuantity, String quantityUnit,
            BigDecimal minimumPrice, BigDecimal targetPrice, String currency,
            LocalDate start, LocalDate end, String strategy,
            String recommendationSnapshot) {
        SalesPlanEntity value = new SalesPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.cropCycleId = cropCycleId;
        value.traceLotId = traceLotId;
        value.planName = planName;
        value.availableQuantity = availableQuantity;
        value.quantityUnit = quantityUnit;
        value.minimumAcceptablePrice = minimumPrice;
        value.targetPrice = targetPrice;
        value.currencyCode = currency;
        value.salesWindowStart = start;
        value.salesWindowEnd = end;
        value.strategy = strategy;
        value.status = "DRAFT";
        value.recommendationSnapshot = recommendationSnapshot;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("DRAFT")) throw new IllegalStateException("Sales plan is not draft.");
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
    }

    public void activate() {
        if (!status.equals("APPROVED")) throw new IllegalStateException("Sales plan is not approved.");
        status = "ACTIVE";
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public UUID cropCycleId() { return cropCycleId; }
    public UUID traceLotId() { return traceLotId; }
    public BigDecimal availableQuantity() { return availableQuantity; }
    public String quantityUnit() { return quantityUnit; }
    public BigDecimal minimumAcceptablePrice() { return minimumAcceptablePrice; }
    public BigDecimal targetPrice() { return targetPrice; }
    public String currencyCode() { return currencyCode; }
    public String strategy() { return strategy; }
    public String status() { return status; }
    public long version() { return version; }
}
