package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropHealthAssessmentRepository extends JpaRepository<CropHealthAssessmentEntity, UUID> {
    List<CropHealthAssessmentEntity> findByTenantIdAndCropCycleIdOrderByAssessmentDateDesc(UUID tenantId, UUID cycleId);
}
