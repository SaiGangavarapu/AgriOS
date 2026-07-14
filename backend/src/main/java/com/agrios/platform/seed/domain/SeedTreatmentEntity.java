package com.agrios.platform.seed.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "seed_treatment", schema = "seed")
public class SeedTreatmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID seedLotId;
    @Column(nullable = false) private String treatmentType;
    private String productName;
    @Column(nullable = false) private LocalDate treatmentDate;
    @Column(nullable = false) private BigDecimal treatedQuantity;
    @Column(nullable = false) private String quantityUnit;
    @Column(columnDefinition = "text") private String notes;
    private UUID treatedBy;
    @Column(nullable = false) private Instant createdAt;

    protected SeedTreatmentEntity() {}

    public static SeedTreatmentEntity create(UUID lotId, String type, String product,
                                             LocalDate date, BigDecimal quantity,
                                             String unit, String notes, UUID actorId) {
        SeedTreatmentEntity value = new SeedTreatmentEntity();
        value.id = UUID.randomUUID();
        value.seedLotId = lotId;
        value.treatmentType = type;
        value.productName = product;
        value.treatmentDate = date;
        value.treatedQuantity = quantity;
        value.quantityUnit = unit;
        value.notes = notes;
        value.treatedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }
}
