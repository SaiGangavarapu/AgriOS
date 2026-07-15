package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "disease_catalog", schema = "crophealth")
public class DiseaseCatalogEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String commonName;
    private String scientificName;
    @Column(nullable = false) private String diseaseCategory;
    private String causalAgent;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String affectedCropCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String symptoms;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String favorableConditions;
    @Column(columnDefinition = "text") private String transmissionNotes;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private String evidenceGrade;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected DiseaseCatalogEntity() {}

    public static DiseaseCatalogEntity create(UUID tenantId, String code, String commonName,
                                              String scientificName, String category,
                                              String causalAgent, String affectedCrops,
                                              String symptoms, String favorableConditions,
                                              String transmissionNotes, String evidenceGrade) {
        DiseaseCatalogEntity value = new DiseaseCatalogEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.code = code.trim().toUpperCase(Locale.ROOT);
        value.commonName = commonName.trim();
        value.scientificName = scientificName;
        value.diseaseCategory = category;
        value.causalAgent = causalAgent;
        value.affectedCropCodes = affectedCrops;
        value.symptoms = symptoms;
        value.favorableConditions = favorableConditions;
        value.transmissionNotes = transmissionNotes;
        value.status = "DRAFT";
        value.evidenceGrade = evidenceGrade;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void publish() {
        if (!status.equals("DRAFT") && !status.equals("APPROVED")) {
            throw new IllegalStateException("Disease catalog item cannot be published.");
        }
        status = "PUBLISHED";
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String code() { return code; }
    public String commonName() { return commonName; }
    public String scientificName() { return scientificName; }
    public String diseaseCategory() { return diseaseCategory; }
    public String causalAgent() { return causalAgent; }
    public String status() { return status; }
    public String evidenceGrade() { return evidenceGrade; }
}
