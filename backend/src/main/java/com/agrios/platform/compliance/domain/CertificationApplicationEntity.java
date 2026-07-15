package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "certification_application", schema = "compliance")
public class CertificationApplicationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID complianceProfileId;
    @Column(nullable = false) private UUID standardId;
    @Column(nullable = false) private String applicationNumber;
    @Column(nullable = false) private LocalDate applicationDate;
    private String certificationBody;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String requestedScope;
    @Column(nullable = false) private String status;
    private Instant submittedAt;
    private Instant decidedAt;
    @Column(columnDefinition = "text") private String decisionNotes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected CertificationApplicationEntity() {}

    public static CertificationApplicationEntity create(
            UUID tenantId, UUID profileId, UUID standardId,
            String applicationNumber, LocalDate applicationDate,
            String certificationBody, String requestedScope) {
        CertificationApplicationEntity value = new CertificationApplicationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.complianceProfileId = profileId;
        value.standardId = standardId;
        value.applicationNumber = applicationNumber;
        value.applicationDate = applicationDate;
        value.certificationBody = certificationBody;
        value.requestedScope = requestedScope;
        value.status = "DRAFT";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void submit() {
        if (!status.equals("DRAFT")) throw new IllegalStateException("Application is not draft.");
        status = "SUBMITTED";
        submittedAt = Instant.now();
        updatedAt = submittedAt;
    }

    public UUID id() { return id; }
    public UUID complianceProfileId() { return complianceProfileId; }
    public UUID standardId() { return standardId; }
    public String status() { return status; }
    public long version() { return version; }
}
