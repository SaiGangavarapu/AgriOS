package com.agrios.platform.cropplanning.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "suitability_assessment", schema = "cropplanning")
public class SuitabilityAssessmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    private UUID requestedBy;
    @Column(nullable = false) private String seasonCode;
    @Column(nullable = false) private String farmingSystem;
    private UUID soilProfileId;
    private UUID waterProfileId;
    @Column(nullable = false) private String assessmentStatus;
    @Column(nullable = false) private Instant assessedAt;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String assumptionsJson;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;

    @OneToMany(mappedBy = "assessmentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SuitabilityCandidateEntity> candidates = new ArrayList<>();

    protected SuitabilityAssessmentEntity() {}

    public static SuitabilityAssessmentEntity create(
            UUID tenantId, UUID fieldId, UUID actorId, String season,
            String farmingSystem, UUID soilProfileId, UUID waterProfileId,
            String assumptionsJson) {
        SuitabilityAssessmentEntity value = new SuitabilityAssessmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.requestedBy = actorId;
        value.seasonCode = season;
        value.farmingSystem = farmingSystem;
        value.soilProfileId = soilProfileId;
        value.waterProfileId = waterProfileId;
        value.assessmentStatus = "COMPLETED";
        value.assessedAt = Instant.now();
        value.assumptionsJson = assumptionsJson;
        value.createdAt = Instant.now();
        return value;
    }

    public void addCandidate(UUID cropId, UUID varietyId, java.math.BigDecimal score,
                             java.math.BigDecimal confidence, boolean failed,
                             String hardConstraints, String reasons, int rank,
                             Integer duration) {
        candidates.add(SuitabilityCandidateEntity.create(
                id, cropId, varietyId, score, confidence, failed,
                hardConstraints, reasons, rank, duration));
    }

    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public String seasonCode() { return seasonCode; }
    public String farmingSystem() { return farmingSystem; }
    public UUID soilProfileId() { return soilProfileId; }
    public UUID waterProfileId() { return waterProfileId; }
    public String assessmentStatus() { return assessmentStatus; }
    public Instant assessedAt() { return assessedAt; }
    public String assumptionsJson() { return assumptionsJson; }
    public List<SuitabilityCandidateEntity> candidates() { return List.copyOf(candidates); }
}
