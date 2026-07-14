package com.agrios.platform.nutrient.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutrientPlanRepository extends JpaRepository<NutrientPlanEntity, UUID> {
    Optional<NutrientPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<NutrientPlanEntity> findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(UUID tenantId, UUID cropCycleId);
}
