package com.agrios.platform.tasks.domain;
import java.time.Instant; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface RecurringTaskScheduleRepository extends JpaRepository<RecurringTaskScheduleEntity,UUID>{List<RecurringTaskScheduleEntity> findByTenantIdAndActiveTrueAndNextRunAtLessThanEqualOrderByNextRunAt(UUID tenantId,Instant due);Optional<RecurringTaskScheduleEntity> findByIdAndTenantId(UUID id,UUID tenantId);}
