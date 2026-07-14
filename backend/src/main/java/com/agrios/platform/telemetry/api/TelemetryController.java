package com.agrios.platform.telemetry.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.telemetry.application.TelemetryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TelemetryController {
    private final TelemetryService service;
    private final TenantContextResolver tenants;

    public TelemetryController(TelemetryService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/telemetry/streams")
    Map<String, UUID> createStream(
            @Valid @RequestBody TelemetryDtos.StreamRequest body,
            HttpServletRequest request) {
        return Map.of("streamId", service.createStream(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/integration/telemetry/readings")
    Map<String, UUID> ingest(
            @Valid @RequestBody TelemetryDtos.ReadingRequest body,
            HttpServletRequest request) {
        return Map.of("readingId", service.ingest(
                tenants.resolve(request).tenantId(), body));
    }

    @GetMapping("/telemetry/streams/{streamId}/latest")
    TelemetryDtos.LatestReadingResponse latest(
            @PathVariable UUID streamId, HttpServletRequest request) {
        return service.latest(tenants.resolve(request).tenantId(), streamId);
    }
}
