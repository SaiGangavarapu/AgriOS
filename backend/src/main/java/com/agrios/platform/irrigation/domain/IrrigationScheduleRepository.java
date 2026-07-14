package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IrrigationScheduleRepository extends JpaRepository<IrrigationScheduleEntity, UUID> {
    List<IrrigationScheduleEntity> findByCropCycleIdOrderByScheduledAtAsc(UUID cycleId);
}
