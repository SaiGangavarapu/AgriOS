package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "knowledge_chunk", schema = "ai")
public class KnowledgeChunkEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID knowledgeDocumentId;
    @Column(nullable = false) private int chunkIndex;
    @Column(nullable = false, columnDefinition = "text") private String chunkText;
    private Integer tokenCount;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;
    private String embeddingModelCode;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String embeddingVector;
    @Column(nullable = false) private Instant createdAt;

    protected KnowledgeChunkEntity() {}

    public static KnowledgeChunkEntity create(
            UUID tenantId, UUID documentId, int chunkIndex,
            String chunkText, Integer tokenCount, String metadata,
            String embeddingModelCode, String embeddingVector) {
        KnowledgeChunkEntity value = new KnowledgeChunkEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.knowledgeDocumentId = documentId;
        value.chunkIndex = chunkIndex;
        value.chunkText = chunkText;
        value.tokenCount = tokenCount;
        value.metadata = metadata;
        value.embeddingModelCode = embeddingModelCode;
        value.embeddingVector = embeddingVector;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String chunkText() { return chunkText; }
    public int chunkIndex() { return chunkIndex; }
}
