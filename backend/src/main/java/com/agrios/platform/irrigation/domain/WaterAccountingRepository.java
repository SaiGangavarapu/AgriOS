package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WaterAccountingRepository extends JpaRepository<WaterAccountingEntity, UUID> {
    Optional<WaterAccountingEntity> findByCropCycleIdAndWaterSourceId(UUID cycleId, UUID sourceId);
    void deleteByCropCycleId(UUID cycleId);
}
