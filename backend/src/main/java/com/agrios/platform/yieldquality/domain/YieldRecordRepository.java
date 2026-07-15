package com.agrios.platform.yieldquality.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface YieldRecordRepository extends JpaRepository<YieldRecordEntity, UUID> {
    boolean existsByHarvestBatchId(UUID harvestBatchId);
}
