package com.agrios.platform.traceability.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "package", schema = "traceability")
public class PackageEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID traceLotId;
    @Column(nullable = false) private String packageCode;
    @Column(nullable = false) private String packageType;
    @Column(nullable = false) private BigDecimal packedQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private Instant packedAt;
    private LocalDate bestBeforeDate;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected PackageEntity() {}

    public static PackageEntity create(
            UUID tenantId, UUID lotId, String code, String type,
            BigDecimal quantity, String unit, LocalDate bestBefore) {
        PackageEntity value = new PackageEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.traceLotId = lotId;
        value.packageCode = code;
        value.packageType = type;
        value.packedQuantity = quantity;
        value.quantityUnit = unit;
        value.packedAt = Instant.now();
        value.bestBeforeDate = bestBefore;
        value.status = "PACKED";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
