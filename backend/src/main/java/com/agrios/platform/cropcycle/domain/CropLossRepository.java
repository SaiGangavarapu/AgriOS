package com.agrios.platform.cropcycle.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropLossRepository extends JpaRepository<CropLossEntity, UUID> {
    List<CropLossEntity> findByCropCycleIdOrderByObservedAtDesc(UUID cropCycleId);
}
