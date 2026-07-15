package com.agrios.platform.finance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CostCenterRepository extends JpaRepository<CostCenterEntity, UUID> {
    Optional<CostCenterEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
