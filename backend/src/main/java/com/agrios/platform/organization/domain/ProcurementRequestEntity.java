package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "procurement_request", schema = "organization")
public class ProcurementRequestEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID organizationId;
    @Column(nullable = false) private String requestCode;
    @Column(nullable = false) private String itemCategory;
    @Column(nullable = false) private String itemName;
    @Column(nullable = false) private BigDecimal targetQuantity;
    @Column(nullable = false) private String quantityUnit;
    private LocalDate requiredBy;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected ProcurementRequestEntity() {}

    public static ProcurementRequestEntity create(
            UUID tenantId, UUID organizationId, String requestCode,
            String itemCategory, String itemName, BigDecimal targetQuantity,
            String quantityUnit, LocalDate requiredBy) {
        ProcurementRequestEntity value = new ProcurementRequestEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationId = organizationId;
        value.requestCode = requestCode;
        value.itemCategory = itemCategory;
        value.itemName = itemName;
        value.targetQuantity = targetQuantity;
        value.quantityUnit = quantityUnit;
        value.requiredBy = requiredBy;
        value.status = "OPEN";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
