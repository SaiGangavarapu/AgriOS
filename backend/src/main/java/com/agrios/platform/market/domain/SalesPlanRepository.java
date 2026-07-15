package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SalesPlanRepository extends JpaRepository<SalesPlanEntity, UUID> {
    Optional<SalesPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SalesPlanEntity> findByTenantIdAndFarmerIdOrderByCreatedAtDesc(UUID tenantId, UUID farmerId);
}
