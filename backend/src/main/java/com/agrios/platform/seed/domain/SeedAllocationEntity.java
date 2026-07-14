package com.agrios.platform.seed.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "seed_allocation", schema = "seed")
public class SeedAllocationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID seedLotId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private BigDecimal allocatedQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(nullable = false) private Instant allocatedAt;
    private UUID allocatedBy;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected SeedAllocationEntity() {}

    public static SeedAllocationEntity create(UUID lotId, UUID cycleId,
                                              BigDecimal quantity, String unit,
                                              UUID actorId) {
        SeedAllocationEntity value = new SeedAllocationEntity();
        value.id = UUID.randomUUID();
        value.seedLotId = lotId;
        value.cropCycleId = cycleId;
        value.allocatedQuantity = quantity;
        value.quantityUnit = unit;
        value.allocatedAt = Instant.now();
        value.allocatedBy = actorId;
        value.status = "ALLOCATED";
        value.createdAt = Instant.now();
        return value;
    }
}
