package com.agrios.platform.organization.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FarmerOrganizationRepository extends JpaRepository<FarmerOrganizationEntity, UUID> {
    Optional<FarmerOrganizationEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
