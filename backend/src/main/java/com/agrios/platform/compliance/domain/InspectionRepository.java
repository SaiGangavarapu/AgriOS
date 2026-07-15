package com.agrios.platform.compliance.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InspectionRepository extends JpaRepository<InspectionEntity, UUID> {
    Optional<InspectionEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
