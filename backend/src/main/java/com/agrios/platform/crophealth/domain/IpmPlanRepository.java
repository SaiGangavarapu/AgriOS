package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IpmPlanRepository extends JpaRepository<IpmPlanEntity, UUID> {
    Optional<IpmPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<IpmPlanEntity> findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(UUID tenantId, UUID cycleId);
}
