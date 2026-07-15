package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "scheme_catalog", schema = "compliance")
public class SchemeCatalogEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String schemeCode;
    @Column(nullable = false) private String schemeName;
    @Column(nullable = false) private String schemeType;
    @Column(nullable = false) private String authorityName;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String geographyCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String eligibilityRules;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String benefitDefinition;
    private String applicationUrl;
    private LocalDate validFrom;
    private LocalDate validUntil;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected SchemeCatalogEntity() {}

    public static SchemeCatalogEntity create(
            UUID tenantId, String code, String name, String type,
            String authority, String geographies, String rules,
            String benefits, String applicationUrl,
            LocalDate validFrom, LocalDate validUntil) {
        SchemeCatalogEntity value = new SchemeCatalogEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.schemeCode = code;
        value.schemeName = name;
        value.schemeType = type;
        value.authorityName = authority;
        value.geographyCodes = geographies;
        value.eligibilityRules = rules;
        value.benefitDefinition = benefits;
        value.applicationUrl = applicationUrl;
        value.validFrom = validFrom;
        value.validUntil = validUntil;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String eligibilityRules() { return eligibilityRules; }
}
