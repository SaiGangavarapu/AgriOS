package com.agrios.platform.operations.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FarmOperationRepository extends JpaRepository<FarmOperationEntity, UUID> {
    Optional<FarmOperationEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<FarmOperationEntity> findByTenantIdAndCropCycleIdOrderByOperationDateDesc(
            UUID tenantId, UUID cropCycleId);
}
