package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AssistantMessageRepository extends JpaRepository<AssistantMessageEntity, UUID> {
    List<AssistantMessageEntity> findByAssistantSessionIdOrderByCreatedAt(UUID sessionId);
}
