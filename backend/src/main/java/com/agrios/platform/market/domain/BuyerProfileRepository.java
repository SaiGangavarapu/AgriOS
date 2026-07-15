package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BuyerProfileRepository extends JpaRepository<BuyerProfileEntity, UUID> {
    Optional<BuyerProfileEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
