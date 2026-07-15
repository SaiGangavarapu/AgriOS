package com.agrios.platform.yieldquality.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QualityAssessmentRepository extends JpaRepository<QualityAssessmentEntity, UUID> {
    Optional<QualityAssessmentEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
