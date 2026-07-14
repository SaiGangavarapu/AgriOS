package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "expert_profile", schema = "advisory")
public class ExpertProfileEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    private UUID userAccountId;
    @Column(nullable = false) private String displayName;
    @Column(nullable = false) private String expertType;
    private String organizationName;
    @Column(columnDefinition = "text") private String qualificationSummary;
    private String registrationReference;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String geographyCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String cropCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String languageCodes;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected ExpertProfileEntity() {}

    public static ExpertProfileEntity create(
            UUID tenantId, UUID userAccountId, String displayName,
            String expertType, String organizationName,
            String qualificationSummary, String registrationReference,
            String geographyCodes, String cropCodes, String languageCodes) {
        ExpertProfileEntity value = new ExpertProfileEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.userAccountId = userAccountId;
        value.displayName = displayName;
        value.expertType = expertType;
        value.organizationName = organizationName;
        value.qualificationSummary = qualificationSummary;
        value.registrationReference = registrationReference;
        value.geographyCodes = geographyCodes;
        value.cropCodes = cropCodes;
        value.languageCodes = languageCodes;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String displayName() { return displayName; }
    public String expertType() { return expertType; }
    public String status() { return status; }
}
