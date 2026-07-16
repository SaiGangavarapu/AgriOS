package com.agrios.platform.tasks.domain;
import java.time.Instant; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
 Optional<TaskEntity> findByIdAndTenantId(UUID id, UUID tenantId);
 List<TaskEntity> findByTenantIdAndAssignedToAndStatusInOrderByDueAtAsc(UUID tenantId, UUID assignedTo, Collection<String> statuses);
 List<TaskEntity> findByTenantIdAndCropCycleIdOrderByDueAtAsc(UUID tenantId, UUID cropCycleId);
 List<TaskEntity> findByTenantIdAndDueAtBetweenOrderByDueAt(UUID tenantId, Instant from, Instant to);
 List<TaskEntity> findByTenantIdAndStatusInOrderByDueAt(UUID tenantId, Collection<String> statuses);
}
