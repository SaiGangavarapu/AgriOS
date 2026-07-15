package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "collective_collection_lot", schema = "organization")
public class CollectiveCollectionLotEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID organizationId;
    @Column(nullable = false) private String collectionCode;
    @Column(nullable = false) private UUID cropId;
    private UUID varietyId;
    private String gradeCode;
    @Column(nullable = false) private BigDecimal totalQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected CollectiveCollectionLotEntity() {}

    public static CollectiveCollectionLotEntity create(
            UUID tenantId, UUID organizationId, String collectionCode,
            UUID cropId, UUID varietyId, String gradeCode, String unit) {
        CollectiveCollectionLotEntity value = new CollectiveCollectionLotEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationId = organizationId;
        value.collectionCode = collectionCode;
        value.cropId = cropId;
        value.varietyId = varietyId;
        value.gradeCode = gradeCode;
        value.totalQuantity = BigDecimal.ZERO;
        value.quantityUnit = unit;
        value.status = "OPEN";
        value.createdAt = Instant.now();
        return value;
    }

    public void addQuantity(BigDecimal quantity) {
        totalQuantity = totalQuantity.add(quantity);
        status = "COLLECTING";
    }

    public UUID id() { return id; }
    public BigDecimal totalQuantity() { return totalQuantity; }
}
