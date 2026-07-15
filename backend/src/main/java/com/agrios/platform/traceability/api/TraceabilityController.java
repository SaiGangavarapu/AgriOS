package com.agrios.platform.traceability.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.traceability.application.TraceabilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TraceabilityController {
    private final TraceabilityService service;
    private final TenantContextResolver tenants;

    public TraceabilityController(TraceabilityService service,
                                  TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/traceability/lots")
    TraceabilityDtos.LotResponse createLot(
            @Valid @RequestBody TraceabilityDtos.LotRequest body,
            HttpServletRequest request) {
        return service.createLot(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/traceability/lots/{lotId}/packages")
    Map<String, UUID> pack(
            @PathVariable UUID lotId,
            @Valid @RequestBody TraceabilityDtos.PackageRequest body,
            HttpServletRequest request) {
        return Map.of("packageId", service.pack(
                tenants.resolve(request).tenantId(), lotId, body));
    }

    @GetMapping("/traceability/public/{qrToken}")
    TraceabilityDtos.LotResponse publicTrace(
            @PathVariable String qrToken) {
        return service.publicTrace(qrToken);
    }

    @GetMapping("/crop-cycles/{cycleId}/traceability-lots")
    List<TraceabilityDtos.LotResponse> list(
            @PathVariable UUID cycleId, HttpServletRequest request) {
        return service.listByCycle(
                tenants.resolve(request).tenantId(), cycleId);
    }
}
