package com.agrios.platform.nutrient.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutrientPlanItemRepository extends JpaRepository<NutrientPlanItemEntity, UUID> {
    List<NutrientPlanItemEntity> findByNutrientPlanId(UUID planId);
}
