package com.agrios.platform.advisory.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdvisoryActionRepository extends JpaRepository<AdvisoryActionEntity, UUID> {
    List<AdvisoryActionEntity> findByAdvisoryId(UUID advisoryId);
}
