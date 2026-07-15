package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "standard_catalog", schema = "compliance")
public class StandardCatalogEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String standardCode;
    @Column(nullable = false) private String standardName;
    @Column(nullable = false) private String standardType;
    private String issuingAuthority;
    private String jurisdictionCode;
    private String versionLabel;
    private LocalDate effectiveFrom;
    private LocalDate effectiveUntil;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected StandardCatalogEntity() {}

    public static StandardCatalogEntity create(
            UUID tenantId, String code, String name, String type,
            String authority, String jurisdiction, String version,
            LocalDate effectiveFrom, LocalDate effectiveUntil) {
        StandardCatalogEntity value = new StandardCatalogEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.standardCode = code;
        value.standardName = name;
        value.standardType = type;
        value.issuingAuthority = authority;
        value.jurisdictionCode = jurisdiction;
        value.versionLabel = version;
        value.effectiveFrom = effectiveFrom;
        value.effectiveUntil = effectiveUntil;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String standardCode() { return standardCode; }
    public String standardName() { return standardName; }
    public String status() { return status; }
}
