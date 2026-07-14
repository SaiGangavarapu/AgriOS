package com.agrios.platform.farm.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterSourceRepository extends JpaRepository<WaterSourceEntity, UUID> {
    List<WaterSourceEntity> findByTenantIdAndFarmId(UUID tenantId, UUID farmId);
}
