package com.agrios.platform.cropcycle.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "season_closure", schema = "cropcycle")
public class SeasonClosureEntity {
    @Id private UUID id;
    @Column(nullable = false, unique = true) private UUID cropCycleId;
    private BigDecimal harvestedQuantity;
    private String harvestedUnit;
    private BigDecimal marketableQuantity;
    private BigDecimal retainedSeedQuantity;
    private BigDecimal householdUseQuantity;
    @Column(columnDefinition = "text") private String closingNotes;
    @Column(nullable = false) private Instant closedAt;
    private UUID closedBy;
    @Column(nullable = false) private Instant createdAt;

    protected SeasonClosureEntity() {}

    public static SeasonClosureEntity create(UUID cycleId, BigDecimal harvested,
                                             String unit, BigDecimal marketable,
                                             BigDecimal retainedSeed,
                                             BigDecimal householdUse,
                                             String notes, UUID actorId) {
        SeasonClosureEntity value = new SeasonClosureEntity();
        value.id = UUID.randomUUID();
        value.cropCycleId = cycleId;
        value.harvestedQuantity = harvested;
        value.harvestedUnit = unit;
        value.marketableQuantity = marketable;
        value.retainedSeedQuantity = retainedSeed;
        value.householdUseQuantity = householdUse;
        value.closingNotes = notes;
        value.closedAt = Instant.now();
        value.closedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }
}
