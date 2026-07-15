package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "compliance_obligation", schema = "compliance")
public class ComplianceObligationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID complianceProfileId;
    @Column(nullable = false) private UUID standardId;
    @Column(nullable = false) private String obligationStatus;
    @Column(nullable = false) private LocalDate applicableFrom;
    private LocalDate applicableUntil;
    @Column(columnDefinition = "text") private String exemptionReason;
    private Instant lastAssessedAt;
    private LocalDate nextAssessmentDue;
    @Column(nullable = false) private Instant createdAt;

    protected ComplianceObligationEntity() {}

    public static ComplianceObligationEntity create(
            UUID tenantId, UUID profileId, UUID standardId,
            LocalDate applicableFrom, LocalDate applicableUntil,
            LocalDate nextAssessmentDue) {
        ComplianceObligationEntity value = new ComplianceObligationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.complianceProfileId = profileId;
        value.standardId = standardId;
        value.obligationStatus = "APPLICABLE";
        value.applicableFrom = applicableFrom;
        value.applicableUntil = applicableUntil;
        value.nextAssessmentDue = nextAssessmentDue;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
