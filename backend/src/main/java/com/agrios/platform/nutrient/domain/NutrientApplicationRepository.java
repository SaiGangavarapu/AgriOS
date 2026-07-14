package com.agrios.platform.nutrient.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutrientApplicationRepository extends JpaRepository<NutrientApplicationEntity, UUID> {
    List<NutrientApplicationEntity> findByCropCycleId(UUID cropCycleId);
}
