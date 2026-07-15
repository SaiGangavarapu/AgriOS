package com.agrios.platform.organization.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SharedResourceRepository extends JpaRepository<SharedResourceEntity, UUID> {
    Optional<SharedResourceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
