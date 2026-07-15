package com.agrios.platform.yieldquality.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QualityParameterRepository extends JpaRepository<QualityParameterEntity, UUID> {
    Optional<QualityParameterEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<QualityParameterEntity> findByTenantIdAndCropIdAndStatus(UUID tenantId, UUID cropId, String status);
}
