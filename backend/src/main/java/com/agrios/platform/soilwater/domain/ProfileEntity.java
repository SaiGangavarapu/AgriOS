package com.agrios.platform.soilwater.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "profile", schema = "soilwater")
public class ProfileEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String profileType;
    private UUID fieldId;
    private UUID waterSourceId;
    @Column(nullable = false) private UUID sourceReportId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String summaryJson;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String constraintCodes;
    @Column(nullable = false) private Instant effectiveFrom;
    private LocalDate validUntil;
    @Column(nullable = false) private boolean isCurrent;
    @Column(nullable = false) private Instant createdAt;

    protected ProfileEntity() {}

    public static ProfileEntity create(UUID tenantId, SampleEntity sample,
                                       UUID reportId, String summaryJson,
                                       String constraintCodes, LocalDate validUntil) {
        ProfileEntity profile = new ProfileEntity();
        profile.id = UUID.randomUUID();
        profile.tenantId = tenantId;
        profile.profileType = sample.sampleType().name();
        profile.fieldId = sample.fieldId();
        profile.waterSourceId = sample.waterSourceId();
        profile.sourceReportId = reportId;
        profile.summaryJson = summaryJson;
        profile.constraintCodes = constraintCodes;
        profile.effectiveFrom = Instant.now();
        profile.validUntil = validUntil;
        profile.isCurrent = true;
        profile.createdAt = Instant.now();
        return profile;
    }

    public void retire() { isCurrent = false; }

    public UUID id() { return id; }
    public String profileType() { return profileType; }
    public UUID fieldId() { return fieldId; }
    public UUID waterSourceId() { return waterSourceId; }
    public UUID sourceReportId() { return sourceReportId; }
    public String summaryJson() { return summaryJson; }
    public String constraintCodes() { return constraintCodes; }
    public Instant effectiveFrom() { return effectiveFrom; }
    public LocalDate validUntil() { return validUntil; }
    public boolean current() { return isCurrent; }
}
