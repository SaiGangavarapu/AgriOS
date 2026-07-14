package com.agrios.platform.operations.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.operations.application.OperationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OperationController {
    private final OperationService service;
    private final TenantContextResolver tenants;

    public OperationController(OperationService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/farm-operations")
    OperationDtos.Response create(@Valid @RequestBody OperationDtos.CreateRequest body,
                                  HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/farm-operations/{operationId}/start")
    OperationDtos.Response start(@PathVariable UUID operationId,
                                 HttpServletRequest request) {
        return service.start(
                tenants.resolve(request).tenantId(), actorId(), operationId);
    }

    @PostMapping("/farm-operations/{operationId}/complete")
    OperationDtos.Response complete(@PathVariable UUID operationId,
                                    HttpServletRequest request) {
        return service.complete(
                tenants.resolve(request).tenantId(), actorId(), operationId);
    }

    @GetMapping("/crop-cycles/{cycleId}/farm-operations")
    List<OperationDtos.Response> list(@PathVariable UUID cycleId,
                                      HttpServletRequest request) {
        return service.list(tenants.resolve(request).tenantId(), cycleId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
