package com.agrios.platform.farm.domain;

import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<FarmEntity, UUID> {
    Optional<FarmEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<FarmEntity> findByTenantId(UUID tenantId, Pageable pageable);
    Page<FarmEntity> findByTenantIdAndPrimaryOperatorFarmerId(
            UUID tenantId, UUID farmerId, Pageable pageable);
}
