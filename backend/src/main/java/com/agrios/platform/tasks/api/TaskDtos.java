package com.agrios.platform.tasks.api;

import com.agrios.platform.tasks.domain.TaskEntity;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public final class TaskDtos {
    private TaskDtos() {}

    public record CreateRequest(
            @NotBlank String ownerContext,
            @NotNull UUID ownerEntityId,
            UUID cropCycleId,
            @NotBlank String taskType,
            @NotBlank @Size(max = 240) String title,
            String description,
            @NotBlank String priority,
            Instant dueAt,
            UUID assignedTo) {}

    public record DeferRequest(@NotNull Instant newDueAt, String reason) {}
    public record CompleteRequest(
            String completionReferenceType,
            UUID completionReferenceId,
            String notes) {}

    public record Response(
            UUID id, String ownerContext, UUID ownerEntityId,
            UUID cropCycleId, String taskType, String title,
            String description, String priority, Instant dueAt,
            UUID assignedTo, String status, Instant completedAt,
            String completionReferenceType, UUID completionReferenceId,
            long version) {
        public static Response from(TaskEntity value) {
            return new Response(value.id(), value.ownerContext(),
                    value.ownerEntityId(), value.cropCycleId(),
                    value.taskType(), value.title(), value.description(),
                    value.priority(), value.dueAt(), value.assignedTo(),
                    value.status(), value.completedAt(),
                    value.completionReferenceType(),
                    value.completionReferenceId(), value.version());
        }
    }
}
