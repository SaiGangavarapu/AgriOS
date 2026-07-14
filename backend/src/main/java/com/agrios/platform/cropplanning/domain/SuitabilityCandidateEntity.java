package com.agrios.platform.cropplanning.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "suitability_candidate", schema = "cropplanning")
public class SuitabilityCandidateEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID assessmentId;
    @Column(nullable = false) private UUID cropId;
    private UUID varietyId;
    @Column(nullable = false) private BigDecimal suitabilityScore;
    @Column(nullable = false) private BigDecimal confidenceScore;
    @Column(nullable = false) private boolean hardConstraintFailed;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String hardConstraintCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @Column(nullable = false) private int rankNo;
    private Integer estimatedDurationDays;
    @Column(nullable = false) private Instant createdAt;

    protected SuitabilityCandidateEntity() {}

    static SuitabilityCandidateEntity create(
            UUID assessmentId, UUID cropId, UUID varietyId,
            BigDecimal score, BigDecimal confidence, boolean failed,
            String hardConstraints, String reasons, int rank, Integer duration) {
        SuitabilityCandidateEntity value = new SuitabilityCandidateEntity();
        value.id = UUID.randomUUID();
        value.assessmentId = assessmentId;
        value.cropId = cropId;
        value.varietyId = varietyId;
        value.suitabilityScore = score;
        value.confidenceScore = confidence;
        value.hardConstraintFailed = failed;
        value.hardConstraintCodes = hardConstraints;
        value.reasonCodes = reasons;
        value.rankNo = rank;
        value.estimatedDurationDays = duration;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID cropId() { return cropId; }
    public UUID varietyId() { return varietyId; }
    public BigDecimal suitabilityScore() { return suitabilityScore; }
    public BigDecimal confidenceScore() { return confidenceScore; }
    public boolean hardConstraintFailed() { return hardConstraintFailed; }
    public String hardConstraintCodes() { return hardConstraintCodes; }
    public String reasonCodes() { return reasonCodes; }
    public int rankNo() { return rankNo; }
    public Integer estimatedDurationDays() { return estimatedDurationDays; }
}
