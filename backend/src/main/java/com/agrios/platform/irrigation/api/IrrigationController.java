package com.agrios.platform.irrigation.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.irrigation.application.IrrigationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IrrigationController {
    private final IrrigationService service;
    private final TenantContextResolver tenants;

    public IrrigationController(IrrigationService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/irrigation-plans/generate")
    IrrigationDtos.PlanResponse generate(
            @Valid @RequestBody IrrigationDtos.GeneratePlanRequest body,
            HttpServletRequest request) {
        return service.generate(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/irrigation-plans/{planId}/approve")
    IrrigationDtos.PlanResponse approve(
            @PathVariable UUID planId, HttpServletRequest request) {
        return service.approve(tenants.resolve(request).tenantId(), actorId(), planId);
    }

    @PostMapping("/irrigation-plans/{planId}/schedules")
    IrrigationDtos.ScheduleResponse schedule(
            @PathVariable UUID planId,
            @Valid @RequestBody IrrigationDtos.ScheduleRequest body,
            HttpServletRequest request) {
        return service.schedule(tenants.resolve(request).tenantId(), planId, body);
    }

    @PostMapping("/irrigation-schedules/{scheduleId}/defer")
    IrrigationDtos.ScheduleResponse defer(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody IrrigationDtos.DeferRequest body,
            HttpServletRequest request) {
        return service.defer(tenants.resolve(request).tenantId(), scheduleId, body);
    }

    @PostMapping("/irrigation-schedules/{scheduleId}/skip")
    IrrigationDtos.ScheduleResponse skip(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody IrrigationDtos.SkipRequest body,
            HttpServletRequest request) {
        return service.skip(tenants.resolve(request).tenantId(), scheduleId, body);
    }

    @PostMapping("/irrigation-schedules/{scheduleId}/executions")
    Map<String, UUID> execute(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody IrrigationDtos.ExecutionRequest body,
            HttpServletRequest request) {
        return Map.of("executionId", service.execute(
                tenants.resolve(request).tenantId(), actorId(), scheduleId, body));
    }

    @GetMapping("/crop-cycles/{cycleId}/irrigation-plans")
    List<IrrigationDtos.PlanResponse> plans(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.listPlans(tenants.resolve(request).tenantId(), cycleId);
    }

    @GetMapping("/crop-cycles/{cycleId}/irrigation-schedules")
    List<IrrigationDtos.ScheduleResponse> schedules(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.listSchedules(tenants.resolve(request).tenantId(), cycleId);
    }

    @GetMapping("/crop-cycles/{cycleId}/water-accounting")
    IrrigationDtos.AccountingResponse accounting(
            @PathVariable UUID cycleId,
            @RequestParam UUID waterSourceId,
            HttpServletRequest request) {
        return service.accounting(
                tenants.resolve(request).tenantId(), cycleId, waterSourceId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
