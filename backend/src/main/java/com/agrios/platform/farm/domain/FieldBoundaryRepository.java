package com.agrios.platform.farm.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldBoundaryRepository extends JpaRepository<FieldBoundaryEntity, UUID> {
    Optional<FieldBoundaryEntity> findByFieldIdAndIsCurrentTrue(UUID fieldId);
    List<FieldBoundaryEntity> findByFieldIdOrderByVersionNoDesc(UUID fieldId);
}
