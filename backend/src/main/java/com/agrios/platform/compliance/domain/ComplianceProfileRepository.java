package com.agrios.platform.compliance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ComplianceProfileRepository extends JpaRepository<ComplianceProfileEntity, UUID> {
    Optional<ComplianceProfileEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
