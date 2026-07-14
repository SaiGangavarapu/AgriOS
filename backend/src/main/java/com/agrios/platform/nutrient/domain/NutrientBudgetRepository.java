package com.agrios.platform.nutrient.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutrientBudgetRepository extends JpaRepository<NutrientBudgetEntity, UUID> {
    List<NutrientBudgetEntity> findByCropCycleId(UUID cropCycleId);
    void deleteByCropCycleId(UUID cropCycleId);
}
