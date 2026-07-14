package com.agrios.platform.tasks.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.tasks.api.TaskDtos;
import com.agrios.platform.tasks.domain.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private static final Set<String> ACTIVE =
            Set.of("OPEN", "ASSIGNED", "IN_PROGRESS", "DEFERRED");

    private final TaskRepository tasks;
    private final TaskEventRepository events;
    private final CropCycleRepository cycles;

    public TaskService(TaskRepository tasks, TaskEventRepository events,
                       CropCycleRepository cycles) {
        this.tasks = tasks;
        this.events = events;
        this.cycles = cycles;
    }

    @Transactional
    public TaskDtos.Response create(UUID tenantId, UUID actorId,
                                    TaskDtos.CreateRequest request) {
        if (request.cropCycleId() != null) {
            cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        }
        TaskEntity task = tasks.save(TaskEntity.create(
                tenantId, request.ownerContext(), request.ownerEntityId(),
                request.cropCycleId(), request.taskType(), request.title(),
                request.description(), request.priority(), request.dueAt(),
                request.assignedTo(), actorId));
        events.save(TaskEventEntity.create(task.id(), "CREATED", actorId, null, "{}"));
        return TaskDtos.Response.from(task);
    }

    @Transactional
    public TaskDtos.Response start(UUID tenantId, UUID actorId, UUID taskId) {
        TaskEntity task = requireTask(tenantId, taskId);
        task.start(actorId);
        events.save(TaskEventEntity.create(task.id(), "STARTED", actorId, null, "{}"));
        return TaskDtos.Response.from(task);
    }

    @Transactional
    public TaskDtos.Response defer(UUID tenantId, UUID actorId, UUID taskId,
                                   TaskDtos.DeferRequest request) {
        TaskEntity task = requireTask(tenantId, taskId);
        task.defer(request.newDueAt(), actorId);
        events.save(TaskEventEntity.create(
                task.id(), "DEFERRED", actorId, request.reason(), "{}"));
        return TaskDtos.Response.from(task);
    }

    @Transactional
    public TaskDtos.Response complete(UUID tenantId, UUID actorId, UUID taskId,
                                      TaskDtos.CompleteRequest request) {
        TaskEntity task = requireTask(tenantId, taskId);
        task.complete(request.completionReferenceType(),
                request.completionReferenceId(), actorId);
        events.save(TaskEventEntity.create(
                task.id(), "COMPLETED", actorId, request.notes(), "{}"));
        return TaskDtos.Response.from(task);
    }

    @Transactional(readOnly = true)
    public List<TaskDtos.Response> activeForAssignee(
            UUID tenantId, UUID assigneeId) {
        return tasks.findByTenantIdAndAssignedToAndStatusInOrderByDueAtAsc(
                        tenantId, assigneeId, ACTIVE)
                .stream().map(TaskDtos.Response::from).toList();
    }

    @Transactional(readOnly = true)
    public List<TaskDtos.Response> byCycle(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return tasks.findByTenantIdAndCropCycleIdOrderByDueAtAsc(tenantId, cycleId)
                .stream().map(TaskDtos.Response::from).toList();
    }

    private TaskEntity requireTask(UUID tenantId, UUID taskId) {
        return tasks.findByIdAndTenantId(taskId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TASK_NOT_FOUND", "Task not found."));
    }
}
