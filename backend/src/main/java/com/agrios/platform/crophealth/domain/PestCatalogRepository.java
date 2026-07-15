package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PestCatalogRepository extends JpaRepository<PestCatalogEntity, UUID> {
    Optional<PestCatalogEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<PestCatalogEntity> findByTenantIdAndStatusOrderByCommonName(UUID tenantId, String status);
}
