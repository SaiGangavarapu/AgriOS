package com.agrios.platform.inventory.domain;
import java.time.LocalDate; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryLotRepository extends JpaRepository<InventoryLotEntity,UUID>{
 Optional<InventoryLotEntity> findByIdAndTenantId(UUID id,UUID tenantId);
 List<InventoryLotEntity> findByTenantIdAndInventoryItemIdAndQuantityAvailableGreaterThanOrderByExpiryDateAscReceivedDateAsc(UUID tenantId,UUID itemId,java.math.BigDecimal zero);
 List<InventoryLotEntity> findByTenantIdAndExpiryDateBetweenAndQuantityAvailableGreaterThanOrderByExpiryDateAsc(UUID tenantId,LocalDate from,LocalDate to,java.math.BigDecimal zero);
}
