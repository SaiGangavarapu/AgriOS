package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "knowledge_source", schema = "ai")
public class KnowledgeSourceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String sourceCode;
    @Column(nullable = false) private String sourceName;
    @Column(nullable = false) private String sourceType;
    @Column(nullable = false) private String ownershipScope;
    private String sourceReference;
    @Column(nullable = false) private String languageCode;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected KnowledgeSourceEntity() {}

    public static KnowledgeSourceEntity create(
            UUID tenantId, String sourceCode, String sourceName,
            String sourceType, String ownershipScope,
            String sourceReference, String languageCode) {
        KnowledgeSourceEntity value = new KnowledgeSourceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceCode = sourceCode;
        value.sourceName = sourceName;
        value.sourceType = sourceType;
        value.ownershipScope = ownershipScope;
        value.sourceReference = sourceReference;
        value.languageCode = languageCode;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
}
