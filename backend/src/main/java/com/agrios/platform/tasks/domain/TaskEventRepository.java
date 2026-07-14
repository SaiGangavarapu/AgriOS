package com.agrios.platform.tasks.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskEventRepository extends JpaRepository<TaskEventEntity, UUID> {}
