package com.agrios.platform.tasks.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.tasks.application.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService service;
    private final TenantContextResolver tenants;

    public TaskController(TaskService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/tasks")
    TaskDtos.Response create(@Valid @RequestBody TaskDtos.CreateRequest body,
                             HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/tasks/{taskId}/start")
    TaskDtos.Response start(@PathVariable UUID taskId, HttpServletRequest request) {
        return service.start(tenants.resolve(request).tenantId(), actorId(), taskId);
    }

    @PostMapping("/tasks/{taskId}/defer")
    TaskDtos.Response defer(@PathVariable UUID taskId,
                            @Valid @RequestBody TaskDtos.DeferRequest body,
                            HttpServletRequest request) {
        return service.defer(
                tenants.resolve(request).tenantId(), actorId(), taskId, body);
    }

    @PostMapping("/tasks/{taskId}/complete")
    TaskDtos.Response complete(@PathVariable UUID taskId,
                               @Valid @RequestBody TaskDtos.CompleteRequest body,
                               HttpServletRequest request) {
        return service.complete(
                tenants.resolve(request).tenantId(), actorId(), taskId, body);
    }

    @GetMapping("/tasks")
    List<TaskDtos.Response> active(
            @RequestParam UUID assignedTo, HttpServletRequest request) {
        return service.activeForAssignee(
                tenants.resolve(request).tenantId(), assignedTo);
    }

    @GetMapping("/crop-cycles/{cycleId}/tasks")
    List<TaskDtos.Response> byCycle(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.byCycle(tenants.resolve(request).tenantId(), cycleId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
