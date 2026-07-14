package com.agrios.platform.seed.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "germination_test", schema = "seed")
public class GerminationTestEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID seedLotId;
    @Column(nullable = false) private Instant testedAt;
    @Column(nullable = false) private int sampleSize;
    @Column(nullable = false) private int germinatedCount;
    @Column(nullable = false) private BigDecimal germinationPercent;
    @Column(nullable = false) private String method;
    private UUID testedBy;
    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false) private Instant createdAt;

    protected GerminationTestEntity() {}

    public static GerminationTestEntity create(UUID lotId, Instant testedAt,
                                               int sampleSize, int germinated,
                                               String method, String notes,
                                               UUID actorId) {
        GerminationTestEntity value = new GerminationTestEntity();
        value.id = UUID.randomUUID();
        value.seedLotId = lotId;
        value.testedAt = testedAt;
        value.sampleSize = sampleSize;
        value.germinatedCount = germinated;
        value.germinationPercent = BigDecimal.valueOf(germinated)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(sampleSize), 4, RoundingMode.HALF_UP);
        value.method = method;
        value.testedBy = actorId;
        value.notes = notes;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal germinationPercent() { return germinationPercent; }
}
