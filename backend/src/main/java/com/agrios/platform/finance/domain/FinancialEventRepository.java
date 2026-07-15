package com.agrios.platform.finance.domain;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FinancialEventRepository extends JpaRepository<FinancialEventEntity, UUID> {
    boolean existsByTenantIdAndIdempotencyKey(UUID tenantId, String idempotencyKey);
    List<FinancialEventEntity> findByTenantIdAndFarmerIdAndEventDateBetweenAndStatus(
            UUID tenantId, UUID farmerId, LocalDate start, LocalDate end, String status);
}
