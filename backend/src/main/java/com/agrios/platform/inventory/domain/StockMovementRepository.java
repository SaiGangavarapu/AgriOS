package com.agrios.platform.inventory.domain;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface StockMovementRepository extends JpaRepository<StockMovementEntity,UUID>{ boolean existsByTenantIdAndIdempotencyKey(UUID tenantId,String key); }
