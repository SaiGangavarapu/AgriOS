package com.agrios.platform.crophealth.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DiseaseCatalogRepository extends JpaRepository<DiseaseCatalogEntity, UUID> {
    Optional<DiseaseCatalogEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<DiseaseCatalogEntity> findByTenantIdAndStatusOrderByCommonName(UUID tenantId, String status);
}
