package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketplaceListingRepository extends JpaRepository<MarketplaceListingEntity, UUID> {
    boolean existsByTenantIdAndIdempotencyKey(UUID tenantId, String idempotencyKey);
}
