package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "scheme_eligibility_assessment", schema = "compliance")
public class SchemeEligibilityAssessmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    @Column(nullable = false) private UUID schemeId;
    @Column(nullable = false) private Instant assessedAt;
    @Column(nullable = false) private String eligibilityStatus;
    private BigDecimal eligibilityScore;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String reasonCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String missingInformation;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceSnapshot;
    @Column(nullable = false) private String assessmentVersion;

    protected SchemeEligibilityAssessmentEntity() {}

    public static SchemeEligibilityAssessmentEntity create(
            UUID tenantId, UUID farmerId, UUID schemeId,
            String status, BigDecimal score, String reasons,
            String missingInformation, String evidenceSnapshot) {
        SchemeEligibilityAssessmentEntity value = new SchemeEligibilityAssessmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.schemeId = schemeId;
        value.assessedAt = Instant.now();
        value.eligibilityStatus = status;
        value.eligibilityScore = score;
        value.reasonCodes = reasons;
        value.missingInformation = missingInformation;
        value.evidenceSnapshot = evidenceSnapshot;
        value.assessmentVersion = "v1";
        return value;
    }

    public UUID id() { return id; }
    public String eligibilityStatus() { return eligibilityStatus; }
    public BigDecimal eligibilityScore() { return eligibilityScore; }
    public String reasonCodes() { return reasonCodes; }
    public String missingInformation() { return missingInformation; }
}
