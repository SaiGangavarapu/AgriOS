package com.agrios.platform.farm.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterSourceRepository extends JpaRepository<WaterSourceEntity, UUID> {
    Optional<WaterSourceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<WaterSourceEntity> findByTenantIdAndFarmId(UUID tenantId, UUID farmId);
}
