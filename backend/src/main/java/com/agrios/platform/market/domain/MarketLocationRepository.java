package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketLocationRepository extends JpaRepository<MarketLocationEntity, UUID> {
    Optional<MarketLocationEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
