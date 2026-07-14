package com.agrios.platform.farm.domain;

import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<FieldEntity, UUID> {
    Optional<FieldEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<FieldEntity> findByTenantIdAndFarmId(UUID tenantId, UUID farmId, Pageable pageable);
}
