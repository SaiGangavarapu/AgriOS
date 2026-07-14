package com.agrios.platform.cropcycle.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "crop_loss", schema = "cropcycle")
public class CropLossEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private String lossType;
    @Column(nullable = false) private BigDecimal affectedAreaHectares;
    private BigDecimal estimatedLossPercent;
    @Column(nullable = false) private String causeCode;
    @Column(nullable = false) private Instant observedAt;
    @Column(columnDefinition = "text") private String notes;
    private UUID recordedBy;
    @Column(nullable = false) private Instant createdAt;

    protected CropLossEntity() {}

    public static CropLossEntity create(UUID cycleId, String lossType,
                                        BigDecimal area, BigDecimal percent,
                                        String cause, Instant observedAt,
                                        String notes, UUID actorId) {
        CropLossEntity value = new CropLossEntity();
        value.id = UUID.randomUUID();
        value.cropCycleId = cycleId;
        value.lossType = lossType;
        value.affectedAreaHectares = area;
        value.estimatedLossPercent = percent;
        value.causeCode = cause;
        value.observedAt = observedAt;
        value.notes = notes;
        value.recordedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String lossType() { return lossType; }
    public BigDecimal affectedAreaHectares() { return affectedAreaHectares; }
    public BigDecimal estimatedLossPercent() { return estimatedLossPercent; }
    public String causeCode() { return causeCode; }
    public Instant observedAt() { return observedAt; }
    public String notes() { return notes; }
}
