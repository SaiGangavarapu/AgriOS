package com.agrios.platform.market.domain;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommodityPriceObservationRepository extends JpaRepository<CommodityPriceObservationEntity, UUID> {
    List<CommodityPriceObservationEntity> findByTenantIdAndCropIdAndObservedDateBetweenOrderByObservedDateDesc(
            UUID tenantId, UUID cropId, LocalDate start, LocalDate end);
}
