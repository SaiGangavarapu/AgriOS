package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "collective_collection_contribution", schema = "organization")
public class CollectiveContributionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID collectionLotId;
    @Column(nullable = false) private UUID membershipId;
    private UUID traceLotId;
    @Column(nullable = false) private BigDecimal contributedQuantity;
    private BigDecimal acceptedQuantity;
    @Column(nullable = false) private BigDecimal rejectedQuantity;
    private String qualityGrade;
    private BigDecimal contributionValue;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected CollectiveContributionEntity() {}

    public static CollectiveContributionEntity create(
            UUID collectionLotId, UUID membershipId, UUID traceLotId,
            BigDecimal contributedQuantity, String qualityGrade) {
        CollectiveContributionEntity value = new CollectiveContributionEntity();
        value.id = UUID.randomUUID();
        value.collectionLotId = collectionLotId;
        value.membershipId = membershipId;
        value.traceLotId = traceLotId;
        value.contributedQuantity = contributedQuantity;
        value.acceptedQuantity = contributedQuantity;
        value.rejectedQuantity = BigDecimal.ZERO;
        value.qualityGrade = qualityGrade;
        value.status = "ACCEPTED";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
