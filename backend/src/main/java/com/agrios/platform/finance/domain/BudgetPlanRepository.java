package com.agrios.platform.finance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BudgetPlanRepository extends JpaRepository<BudgetPlanEntity, UUID> {
    Optional<BudgetPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
