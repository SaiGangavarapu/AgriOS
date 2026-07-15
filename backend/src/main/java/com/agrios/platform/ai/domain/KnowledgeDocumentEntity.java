package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "knowledge_document", schema = "ai")
public class KnowledgeDocumentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID knowledgeSourceId;
    private String externalDocumentId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String contentHash;
    private String mimeType;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;
    private UUID farmerId;
    private UUID farmId;
    private UUID fieldId;
    private UUID cropCycleId;
    @Column(nullable = false) private String status;
    private Instant indexedAt;
    @Column(nullable = false) private Instant createdAt;

    protected KnowledgeDocumentEntity() {}

    public static KnowledgeDocumentEntity create(
            UUID tenantId, UUID sourceId, String externalDocumentId,
            String title, String contentHash, String mimeType,
            String metadata, UUID farmerId, UUID farmId,
            UUID fieldId, UUID cropCycleId) {
        KnowledgeDocumentEntity value = new KnowledgeDocumentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.knowledgeSourceId = sourceId;
        value.externalDocumentId = externalDocumentId;
        value.title = title;
        value.contentHash = contentHash;
        value.mimeType = mimeType;
        value.metadata = metadata;
        value.farmerId = farmerId;
        value.farmId = farmId;
        value.fieldId = fieldId;
        value.cropCycleId = cropCycleId;
        value.status = "READY";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
