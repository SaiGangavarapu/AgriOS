package com.agrios.platform.yieldquality.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QualityResultRepository extends JpaRepository<QualityResultEntity, UUID> {
    List<QualityResultEntity> findByQualityAssessmentId(UUID assessmentId);
}
