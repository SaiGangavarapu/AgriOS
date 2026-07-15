package com.agrios.platform.harvest.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HarvestPlanRepository extends JpaRepository<HarvestPlanEntity, UUID> {
    Optional<HarvestPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<HarvestPlanEntity> findByTenantIdAndCropCycleIdOrderByExpectedStartDateDesc(UUID tenantId, UUID cropCycleId);
}
