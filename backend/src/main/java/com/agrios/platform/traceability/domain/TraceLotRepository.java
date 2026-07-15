package com.agrios.platform.traceability.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TraceLotRepository extends JpaRepository<TraceLotEntity, UUID> {
    Optional<TraceLotEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<TraceLotEntity> findByQrToken(String qrToken);
    List<TraceLotEntity> findByTenantIdAndCropCycleIdOrderByCreatedAtDesc(UUID tenantId, UUID cycleId);
}
