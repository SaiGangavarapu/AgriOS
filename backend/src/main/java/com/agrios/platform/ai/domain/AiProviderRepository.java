package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AiProviderRepository extends JpaRepository<AiProviderEntity, UUID> {
    Optional<AiProviderEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
