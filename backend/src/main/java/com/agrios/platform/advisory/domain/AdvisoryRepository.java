package com.agrios.platform.advisory.domain;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntity, UUID> {
    Optional<AdvisoryEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<AdvisoryEntity> findByTenantIdAndFarmerIdAndStatusOrderByValidFromDesc(
            UUID tenantId, UUID farmerId, String status);
    List<AdvisoryEntity> findByTenantIdAndStatusAndValidFromLessThanEqualOrderByPriorityAsc(
            UUID tenantId, String status, Instant now);
}
