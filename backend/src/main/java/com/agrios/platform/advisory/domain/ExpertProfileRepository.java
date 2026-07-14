package com.agrios.platform.advisory.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpertProfileRepository extends JpaRepository<ExpertProfileEntity, UUID> {
    Optional<ExpertProfileEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ExpertProfileEntity> findByTenantIdAndStatusOrderByDisplayName(UUID tenantId, String status);
}
