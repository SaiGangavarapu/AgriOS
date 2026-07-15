package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IpmActionRepository extends JpaRepository<IpmActionEntity, UUID> {
    List<IpmActionEntity> findByIpmPlanIdOrderByActionSequence(UUID planId);
}
