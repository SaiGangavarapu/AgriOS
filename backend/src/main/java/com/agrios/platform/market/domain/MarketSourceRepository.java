package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketSourceRepository extends JpaRepository<MarketSourceEntity, UUID> {
    Optional<MarketSourceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
