package com.agrios.platform.seed.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SeedLotRepository extends JpaRepository<SeedLotEntity, UUID> {
    Optional<SeedLotEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SeedLotEntity> findByTenantIdAndCropIdAndStatusOrderByCreatedAtDesc(
            UUID tenantId, UUID cropId, String status);
}
