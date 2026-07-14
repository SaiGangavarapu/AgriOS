package com.agrios.platform.farmer.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<FarmerEntity, UUID> {
    Optional<FarmerEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<FarmerEntity> findByTenantId(UUID tenantId, Pageable pageable);
    Page<FarmerEntity> findByTenantIdAndFullNameContainingIgnoreCase(
            UUID tenantId, String search, Pageable pageable);
    boolean existsByTenantIdAndMobileE164AndStatusNot(
            UUID tenantId, String mobileE164, FarmerStatus excludedStatus);
}
