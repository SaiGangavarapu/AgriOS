package com.agrios.platform.cropcycle.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SeasonClosureRepository extends JpaRepository<SeasonClosureEntity, UUID> {
    Optional<SeasonClosureEntity> findByCropCycleId(UUID cropCycleId);
}
