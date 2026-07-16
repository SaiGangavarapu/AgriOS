package com.agrios.platform.operations.domain;
import java.time.LocalDate; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface FarmOperationRepository extends JpaRepository<FarmOperationEntity,UUID>{
 Optional<FarmOperationEntity> findByIdAndTenantId(UUID id,UUID tenantId);
 List<FarmOperationEntity> findByTenantIdAndCropCycleIdOrderByOperationDateDesc(UUID tenantId,UUID cropCycleId);
 List<FarmOperationEntity> findByTenantIdAndOperationDateBetweenOrderByOperationDate(UUID tenantId,LocalDate from,LocalDate to);
 long countByTenantIdAndStatus(UUID tenantId,String status);
}
