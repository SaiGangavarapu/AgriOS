package com.agrios.platform.cropcycle.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stage_observation", schema = "cropcycle")
public class StageObservationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private String stageCode;
    @Column(nullable = false) private Instant observedAt;
    @Column(nullable = false) private String sourceType;
    private BigDecimal confidenceScore;
    @Column(columnDefinition = "text") private String notes;
    private UUID observedBy;
    @Column(nullable = false) private Instant createdAt;

    protected StageObservationEntity() {}

    public static StageObservationEntity create(UUID cycleId, String stageCode,
                                                Instant observedAt, String sourceType,
                                                BigDecimal confidence, String notes,
                                                UUID actorId) {
        StageObservationEntity value = new StageObservationEntity();
        value.id = UUID.randomUUID();
        value.cropCycleId = cycleId;
        value.stageCode = stageCode;
        value.observedAt = observedAt;
        value.sourceType = sourceType;
        value.confidenceScore = confidence;
        value.notes = notes;
        value.observedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String stageCode() { return stageCode; }
    public Instant observedAt() { return observedAt; }
    public String sourceType() { return sourceType; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public String notes() { return notes; }
}
