package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScoutingVisitRepository extends JpaRepository<ScoutingVisitEntity, UUID> {
    Optional<ScoutingVisitEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ScoutingVisitEntity> findByTenantIdAndCropCycleIdOrderByScoutedAtDesc(UUID tenantId, UUID cycleId);
}
