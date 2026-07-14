package com.agrios.platform.soilwater.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LaboratoryRepository extends JpaRepository<LaboratoryEntity, UUID> {
    Optional<LaboratoryEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<LaboratoryEntity> findByTenantIdAndStatusOrderByName(UUID tenantId, String status);
}
