package com.agrios.platform.cropcycle.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.cropcycle.application.CropCycleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CropCycleController {
    private final CropCycleService service;
    private final TenantContextResolver tenants;

    public CropCycleController(CropCycleService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/crop-cycles")
    CropCycleDtos.Response create(@Valid @RequestBody CropCycleDtos.CreateRequest body,
                                  HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/crop-cycles/{cycleId}/activate")
    CropCycleDtos.Response activate(@PathVariable UUID cycleId,
                                    HttpServletRequest request) {
        return service.activate(tenants.resolve(request).tenantId(), actorId(), cycleId);
    }

    @PostMapping("/crop-cycles/{cycleId}/sowing")
    CropCycleDtos.Response sowing(@PathVariable UUID cycleId,
                                  @Valid @RequestBody CropCycleDtos.SowingRequest body,
                                  HttpServletRequest request) {
        return service.recordSowing(
                tenants.resolve(request).tenantId(), actorId(), cycleId, body);
    }

    @PostMapping("/crop-cycles/{cycleId}/stage-observations")
    CropCycleDtos.Response stage(@PathVariable UUID cycleId,
                                 @Valid @RequestBody CropCycleDtos.StageRequest body,
                                 HttpServletRequest request) {
        return service.updateStage(
                tenants.resolve(request).tenantId(), actorId(), cycleId, body);
    }

    @PostMapping("/crop-cycles/{cycleId}/losses")
    CropCycleDtos.Response loss(@PathVariable UUID cycleId,
                                @Valid @RequestBody CropCycleDtos.LossRequest body,
                                HttpServletRequest request) {
        return service.recordLoss(
                tenants.resolve(request).tenantId(), actorId(), cycleId, body);
    }

    @PostMapping("/crop-cycles/{cycleId}/close")
    CropCycleDtos.Response close(@PathVariable UUID cycleId,
                                 @Valid @RequestBody CropCycleDtos.CloseRequest body,
                                 HttpServletRequest request) {
        return service.close(
                tenants.resolve(request).tenantId(), actorId(), cycleId, body);
    }

    @GetMapping("/crop-cycles/{cycleId}")
    CropCycleDtos.Response get(@PathVariable UUID cycleId, HttpServletRequest request) {
        return service.get(tenants.resolve(request).tenantId(), cycleId);
    }

    @GetMapping("/fields/{fieldId}/crop-cycles")
    List<CropCycleDtos.Response> list(@PathVariable UUID fieldId,
                                      HttpServletRequest request) {
        return service.listByField(tenants.resolve(request).tenantId(), fieldId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
