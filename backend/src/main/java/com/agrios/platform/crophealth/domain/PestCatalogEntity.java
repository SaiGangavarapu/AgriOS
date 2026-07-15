package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "pest_catalog", schema = "crophealth")
public class PestCatalogEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String commonName;
    private String scientificName;
    @Column(nullable = false) private String pestCategory;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String affectedCropCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String symptoms;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String favorableConditions;
    @Column(columnDefinition = "text") private String lifecycleNotes;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private String evidenceGrade;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected PestCatalogEntity() {}

    public static PestCatalogEntity create(UUID tenantId, String code, String commonName,
                                           String scientificName, String category,
                                           String affectedCrops, String symptoms,
                                           String favorableConditions,
                                           String lifecycleNotes, String evidenceGrade) {
        PestCatalogEntity value = new PestCatalogEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.code = code.trim().toUpperCase(Locale.ROOT);
        value.commonName = commonName.trim();
        value.scientificName = scientificName;
        value.pestCategory = category;
        value.affectedCropCodes = affectedCrops;
        value.symptoms = symptoms;
        value.favorableConditions = favorableConditions;
        value.lifecycleNotes = lifecycleNotes;
        value.status = "DRAFT";
        value.evidenceGrade = evidenceGrade;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void publish() {
        if (!status.equals("DRAFT") && !status.equals("APPROVED")) {
            throw new IllegalStateException("Pest catalog item cannot be published.");
        }
        status = "PUBLISHED";
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String code() { return code; }
    public String commonName() { return commonName; }
    public String scientificName() { return scientificName; }
    public String pestCategory() { return pestCategory; }
    public String status() { return status; }
    public String evidenceGrade() { return evidenceGrade; }
}
