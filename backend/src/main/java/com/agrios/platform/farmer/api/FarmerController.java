package com.agrios.platform.farmer.api;

import com.agrios.platform.common.api.PageResponse;
import com.agrios.platform.common.web.*;
import com.agrios.platform.farmer.application.FarmerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {
    private final FarmerService service;
    private final TenantContextResolver tenants;

    public FarmerController(FarmerService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping
    ResponseEntity<FarmerDtos.Response> register(
            @Valid @RequestBody FarmerDtos.RegisterRequest request,
            HttpServletRequest servletRequest) {
        UUID tenantId = tenants.resolve(servletRequest).tenantId();
        FarmerDtos.Response response = service.register(
                tenantId, actorId(), correlationId(), request);
        return ResponseEntity.created(URI.create("/api/v1/farmers/" + response.id()))
                .body(response);
    }

    @GetMapping("/{farmerId}")
    FarmerDtos.Response get(@PathVariable UUID farmerId, HttpServletRequest request) {
        return service.get(tenants.resolve(request).tenantId(), farmerId);
    }

    @GetMapping
    PageResponse<FarmerDtos.Response> search(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletRequest request) {
        return PageResponse.from(service.search(
                tenants.resolve(request).tenantId(), q, pageable));
    }

    @PutMapping("/{farmerId}")
    FarmerDtos.Response update(
            @PathVariable UUID farmerId,
            @Valid @RequestBody FarmerDtos.UpdateRequest body,
            HttpServletRequest request) {
        return service.update(tenants.resolve(request).tenantId(),
                actorId(), farmerId, body);
    }

    @PostMapping("/{farmerId}/verify")
    FarmerDtos.Response verify(
            @PathVariable UUID farmerId,
            @Valid @RequestBody FarmerDtos.VerifyRequest body,
            HttpServletRequest request) {
        return service.verify(tenants.resolve(request).tenantId(),
                actorId(), farmerId, body);
    }

    @PostMapping("/{farmerId}/suspend")
    FarmerDtos.Response suspend(@PathVariable UUID farmerId, HttpServletRequest request) {
        return service.suspend(tenants.resolve(request).tenantId(), actorId(), farmerId);
    }

    @PostMapping("/{farmerId}/reactivate")
    FarmerDtos.Response reactivate(@PathVariable UUID farmerId, HttpServletRequest request) {
        return service.reactivate(tenants.resolve(request).tenantId(), actorId(), farmerId);
    }

    @PostMapping("/merge")
    FarmerDtos.Response merge(
            @Valid @RequestBody FarmerDtos.MergeRequest body,
            HttpServletRequest request) {
        return service.merge(tenants.resolve(request).tenantId(), actorId(), body);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }

    private UUID correlationId() {
        String raw = MDC.get(CorrelationIdFilter.MDC_KEY);
        return raw == null ? UUID.randomUUID() : UUID.fromString(raw);
    }
}
