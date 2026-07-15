package com.agrios.platform.market.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BuyerOfferRepository extends JpaRepository<BuyerOfferEntity, UUID> {
    Optional<BuyerOfferEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<BuyerOfferEntity> findByTenantIdAndSalesPlanIdAndStatusOrderByOfferedPriceDesc(
            UUID tenantId, UUID salesPlanId, String status);
}
