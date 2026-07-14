package com.agrios.platform.advisory.api;

import com.agrios.platform.advisory.application.AdvisoryService;
import com.agrios.platform.common.web.TenantContextResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AdvisoryController {
    private final AdvisoryService service;
    private final TenantContextResolver tenants;

    public AdvisoryController(AdvisoryService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/advisories")
    AdvisoryDtos.Response create(
            @Valid @RequestBody AdvisoryDtos.CreateRequest body,
            HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/advisories/{advisoryId}/submit-review")
    AdvisoryDtos.Response submit(@PathVariable UUID advisoryId,
                                 HttpServletRequest request) {
        return service.submit(tenants.resolve(request).tenantId(), actorId(), advisoryId);
    }

    @PostMapping("/advisories/{advisoryId}/approve")
    AdvisoryDtos.Response approve(@PathVariable UUID advisoryId,
                                  HttpServletRequest request) {
        return service.approve(tenants.resolve(request).tenantId(), actorId(), advisoryId);
    }

    @PostMapping("/advisories/{advisoryId}/publish")
    AdvisoryDtos.Response publish(@PathVariable UUID advisoryId,
                                  HttpServletRequest request) {
        return service.publish(tenants.resolve(request).tenantId(), actorId(), advisoryId);
    }

    @PostMapping("/advisories/{advisoryId}/withdraw")
    AdvisoryDtos.Response withdraw(@PathVariable UUID advisoryId,
                                   HttpServletRequest request) {
        return service.withdraw(tenants.resolve(request).tenantId(), actorId(), advisoryId);
    }

    @GetMapping("/farmers/{farmerId}/advisories")
    List<AdvisoryDtos.Response> farmerAdvisories(
            @PathVariable UUID farmerId, HttpServletRequest request) {
        return service.publishedForFarmer(
                tenants.resolve(request).tenantId(), farmerId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
