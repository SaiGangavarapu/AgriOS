package com.agrios.platform.inventory.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, UUID> {
 Optional<InventoryItemEntity> findByIdAndTenantId(UUID id, UUID tenantId);
 boolean existsByTenantIdAndFarmIdAndItemCode(UUID tenantId, UUID farmId, String itemCode);
 List<InventoryItemEntity> findByTenantIdAndFarmIdAndActiveTrueOrderByItemName(UUID tenantId, UUID farmId);
}
