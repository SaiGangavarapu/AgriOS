package com.agrios.platform.seed.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.seed.application.SeedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SeedController {
    private final SeedService service;
    private final TenantContextResolver tenants;

    public SeedController(SeedService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/seed-lots")
    SeedDtos.LotResponse create(@Valid @RequestBody SeedDtos.CreateLotRequest body,
                                HttpServletRequest request) {
        return service.createLot(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/seed-lots/{lotId}/germination-tests")
    SeedDtos.LotResponse germination(@PathVariable UUID lotId,
                                     @Valid @RequestBody SeedDtos.GerminationRequest body,
                                     HttpServletRequest request) {
        return service.germinationTest(
                tenants.resolve(request).tenantId(), actorId(), lotId, body);
    }

    @PostMapping("/seed-lots/{lotId}/treatments")
    SeedDtos.LotResponse treatment(@PathVariable UUID lotId,
                                   @Valid @RequestBody SeedDtos.TreatmentRequest body,
                                   HttpServletRequest request) {
        return service.treat(
                tenants.resolve(request).tenantId(), actorId(), lotId, body);
    }

    @PostMapping("/seed-lots/{lotId}/allocations")
    SeedDtos.LotResponse allocate(@PathVariable UUID lotId,
                                  @Valid @RequestBody SeedDtos.AllocationRequest body,
                                  HttpServletRequest request) {
        return service.allocate(
                tenants.resolve(request).tenantId(), actorId(), lotId, body);
    }

    @GetMapping("/crops/{cropId}/seed-lots")
    List<SeedDtos.LotResponse> available(@PathVariable UUID cropId,
                                         HttpServletRequest request) {
        return service.availableLots(tenants.resolve(request).tenantId(), cropId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
