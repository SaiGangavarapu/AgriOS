package com.agrios.platform.knowledge.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropRequirementRepository extends JpaRepository<CropRequirementEntity, UUID> {
    List<CropRequirementEntity> findByCropId(UUID cropId);
}
