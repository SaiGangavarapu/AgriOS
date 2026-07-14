package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AutomationPolicyRepository extends JpaRepository<AutomationPolicyEntity, UUID> {
    Optional<AutomationPolicyEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<AutomationPolicyEntity> findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(UUID tenantId, UUID cycleId);
}
