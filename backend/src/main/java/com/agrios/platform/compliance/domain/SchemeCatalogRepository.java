package com.agrios.platform.compliance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SchemeCatalogRepository extends JpaRepository<SchemeCatalogEntity, UUID> {
    Optional<SchemeCatalogEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
