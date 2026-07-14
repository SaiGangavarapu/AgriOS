package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IrrigationExecutionRepository extends JpaRepository<IrrigationExecutionEntity, UUID> {
    List<IrrigationExecutionEntity> findByCropCycleId(UUID cycleId);
}
