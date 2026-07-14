package com.agrios.platform.soilwater.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SampleRepository extends JpaRepository<SampleEntity, UUID> {
    Optional<SampleEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SampleEntity> findByTenantIdAndFieldIdOrderByCollectedAtDesc(UUID tenantId, UUID fieldId);
    List<SampleEntity> findByTenantIdAndWaterSourceIdOrderByCollectedAtDesc(UUID tenantId, UUID waterSourceId);
}
