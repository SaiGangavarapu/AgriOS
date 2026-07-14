package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AutomationDecisionRepository extends JpaRepository<AutomationDecisionEntity, UUID> {
    Optional<AutomationDecisionEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
