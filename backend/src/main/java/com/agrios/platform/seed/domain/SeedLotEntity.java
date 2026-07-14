package com.agrios.platform.seed.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "seed_lot", schema = "seed")
public class SeedLotEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropId;
    private UUID varietyId;
    @Column(nullable = false) private String lotCode;
    @Column(nullable = false) private String seedClass;
    private String supplierName;
    private String producerName;
    private BigDecimal germinationPercent;
    private BigDecimal purityPercent;
    @Column(nullable = false) private String treatmentStatus;
    @Column(nullable = false) private BigDecimal quantityAvailable;
    @Column(nullable = false) private String quantityUnit;
    private LocalDate packedOn;
    private LocalDate expiresOn;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected SeedLotEntity() {}

    public static SeedLotEntity create(UUID tenantId, UUID cropId, UUID varietyId,
                                       String lotCode, String seedClass,
                                       String supplier, String producer,
                                       BigDecimal germination, BigDecimal purity,
                                       BigDecimal quantity, String unit,
                                       LocalDate packedOn, LocalDate expiresOn,
                                       UUID actorId) {
        SeedLotEntity value = new SeedLotEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropId = cropId;
        value.varietyId = varietyId;
        value.lotCode = lotCode.trim();
        value.seedClass = seedClass;
        value.supplierName = supplier;
        value.producerName = producer;
        value.germinationPercent = germination;
        value.purityPercent = purity;
        value.treatmentStatus = "UNTREATED";
        value.quantityAvailable = quantity;
        value.quantityUnit = unit;
        value.packedOn = packedOn;
        value.expiresOn = expiresOn;
        value.status = expiresOn != null && expiresOn.isBefore(LocalDate.now())
                ? "EXPIRED" : "AVAILABLE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void updateGermination(BigDecimal percent, UUID actorId) {
        germinationPercent = percent;
        touch(actorId);
    }

    public void markTreated(UUID actorId) {
        treatmentStatus = "TREATED";
        touch(actorId);
    }

    public void allocate(BigDecimal quantity, UUID actorId) {
        if (!"AVAILABLE".equals(status)) {
            throw new IllegalStateException("Seed lot is not available.");
        }
        if (quantityAvailable.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient seed quantity.");
        }
        quantityAvailable = quantityAvailable.subtract(quantity);
        if (quantityAvailable.signum() == 0) status = "EXHAUSTED";
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID cropId() { return cropId; }
    public UUID varietyId() { return varietyId; }
    public String lotCode() { return lotCode; }
    public String seedClass() { return seedClass; }
    public String supplierName() { return supplierName; }
    public String producerName() { return producerName; }
    public BigDecimal germinationPercent() { return germinationPercent; }
    public BigDecimal purityPercent() { return purityPercent; }
    public String treatmentStatus() { return treatmentStatus; }
    public BigDecimal quantityAvailable() { return quantityAvailable; }
    public String quantityUnit() { return quantityUnit; }
    public LocalDate packedOn() { return packedOn; }
    public LocalDate expiresOn() { return expiresOn; }
    public String status() { return status; }
    public long version() { return version; }
}
