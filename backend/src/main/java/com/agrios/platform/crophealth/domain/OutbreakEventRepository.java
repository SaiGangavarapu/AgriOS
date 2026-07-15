package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OutbreakEventRepository extends JpaRepository<OutbreakEventEntity, UUID> {
    Optional<OutbreakEventEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<OutbreakEventEntity> findByTenantIdAndCropCycleIdAndStatusOrderByDetectedAtDesc(
            UUID tenantId, UUID cycleId, String status);
}
