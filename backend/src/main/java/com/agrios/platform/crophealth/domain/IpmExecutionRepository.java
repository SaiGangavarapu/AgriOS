package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IpmExecutionRepository extends JpaRepository<IpmExecutionEntity, UUID> {
    List<IpmExecutionEntity> findByCropCycleIdOrderByExecutedAtDesc(UUID cycleId);
}
