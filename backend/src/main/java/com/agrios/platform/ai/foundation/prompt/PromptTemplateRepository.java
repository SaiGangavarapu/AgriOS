package com.agrios.platform.ai.foundation.prompt;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptTemplateRepository extends JpaRepository<PromptTemplateEntity, UUID> {
    Optional<PromptTemplateEntity> findFirstByTenantIdAndAssistantTypeAndLanguageCodeAndStatusOrderByVersionNoDesc(
            UUID tenantId, String assistantType, String languageCode, String status);
}
