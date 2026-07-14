package com.agrios.platform.weather.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClimateRiskAssessmentRepository extends JpaRepository<ClimateRiskAssessmentEntity, UUID> {
    List<ClimateRiskAssessmentEntity> findByTenantIdAndFieldIdOrderByAssessedAtDesc(UUID tenantId, UUID fieldId);
}
