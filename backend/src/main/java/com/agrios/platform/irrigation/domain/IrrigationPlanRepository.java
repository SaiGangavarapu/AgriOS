package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IrrigationPlanRepository extends JpaRepository<IrrigationPlanEntity, UUID> {
    Optional<IrrigationPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<IrrigationPlanEntity> findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(UUID tenantId, UUID cycleId);
}
