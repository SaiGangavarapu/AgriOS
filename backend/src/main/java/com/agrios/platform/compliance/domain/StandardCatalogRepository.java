package com.agrios.platform.compliance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StandardCatalogRepository extends JpaRepository<StandardCatalogEntity, UUID> {
    Optional<StandardCatalogEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
