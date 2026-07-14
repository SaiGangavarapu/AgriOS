package com.agrios.platform.cropplanning.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AssessmentRepository extends JpaRepository<SuitabilityAssessmentEntity, UUID> {
    Optional<SuitabilityAssessmentEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SuitabilityAssessmentEntity> findByTenantIdAndFieldIdOrderByAssessedAtDesc(
            UUID tenantId, UUID fieldId);
}
