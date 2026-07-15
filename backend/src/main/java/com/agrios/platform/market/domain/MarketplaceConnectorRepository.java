package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketplaceConnectorRepository extends JpaRepository<MarketplaceConnectorEntity, UUID> {
    Optional<MarketplaceConnectorEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
