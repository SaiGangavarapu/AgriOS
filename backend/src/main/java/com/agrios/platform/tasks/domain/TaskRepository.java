package com.agrios.platform.tasks.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    Optional<TaskEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<TaskEntity> findByTenantIdAndAssignedToAndStatusInOrderByDueAtAsc(
            UUID tenantId, UUID assignedTo, Collection<String> statuses);
    List<TaskEntity> findByTenantIdAndCropCycleIdOrderByDueAtAsc(UUID tenantId, UUID cropCycleId);
}
