package com.agrios.platform.soilwater.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TestReportRepository extends JpaRepository<TestReportEntity, UUID> {
    Optional<TestReportEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<TestReportEntity> findByTenantIdAndSampleIdOrderByTestedAtDesc(UUID tenantId, UUID sampleId);
}
