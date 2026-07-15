package com.agrios.platform.harvest.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HarvestBatchRepository extends JpaRepository<HarvestBatchEntity, UUID> {
    Optional<HarvestBatchEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<HarvestBatchEntity> findByTenantIdAndCropCycleIdOrderByHarvestedAtDesc(UUID tenantId, UUID cropCycleId);
}
