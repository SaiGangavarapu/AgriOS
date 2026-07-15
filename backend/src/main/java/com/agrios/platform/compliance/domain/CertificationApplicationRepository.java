package com.agrios.platform.compliance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CertificationApplicationRepository extends JpaRepository<CertificationApplicationEntity, UUID> {
    Optional<CertificationApplicationEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
