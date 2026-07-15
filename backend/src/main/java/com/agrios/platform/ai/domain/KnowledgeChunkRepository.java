package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunkEntity, UUID> {
    List<KnowledgeChunkEntity> findByKnowledgeDocumentIdOrderByChunkIndex(UUID documentId);
}
