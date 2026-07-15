package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScoutingObservationRepository extends JpaRepository<ScoutingObservationEntity, UUID> {
    List<ScoutingObservationEntity> findByScoutingVisitId(UUID visitId);
}
