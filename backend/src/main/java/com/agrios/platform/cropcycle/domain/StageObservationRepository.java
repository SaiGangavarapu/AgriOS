package com.agrios.platform.cropcycle.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StageObservationRepository extends JpaRepository<StageObservationEntity, UUID> {
    List<StageObservationEntity> findByCropCycleIdOrderByObservedAtDesc(UUID cropCycleId);
}
