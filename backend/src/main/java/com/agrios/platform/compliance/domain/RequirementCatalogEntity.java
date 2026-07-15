package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "requirement_catalog", schema = "compliance")
public class RequirementCatalogEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID standardId;
    @Column(nullable = false) private String requirementCode;
    @Column(nullable = false) private String requirementTitle;
    @Column(columnDefinition = "text") private String requirementDescription;
    @Column(nullable = false) private String requirementCategory;
    private String evidenceType;
    @Column(nullable = false) private boolean mandatory;
    @Column(nullable = false) private String severityIfFailed;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String controlJson;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected RequirementCatalogEntity() {}

    public static RequirementCatalogEntity create(
            UUID standardId, String code, String title, String description,
            String category, String evidenceType, boolean mandatory,
            String severityIfFailed, String controlJson) {
        RequirementCatalogEntity value = new RequirementCatalogEntity();
        value.id = UUID.randomUUID();
        value.standardId = standardId;
        value.requirementCode = code;
        value.requirementTitle = title;
        value.requirementDescription = description;
        value.requirementCategory = category;
        value.evidenceType = evidenceType;
        value.mandatory = mandatory;
        value.severityIfFailed = severityIfFailed;
        value.controlJson = controlJson;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
