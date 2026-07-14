package com.agrios.platform.consent.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.consent.application.ConsentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ConsentController {
    private final ConsentService service;
    private final TenantContextResolver tenants;

    public ConsentController(ConsentService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/consents")
    ConsentDtos.Response grant(@Valid @RequestBody ConsentDtos.GrantRequest body,
                               HttpServletRequest request) {
        return service.grant(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @GetMapping("/farmers/{farmerId}/consents")
    List<ConsentDtos.Response> list(@PathVariable UUID farmerId,
                                    HttpServletRequest request) {
        return service.list(tenants.resolve(request).tenantId(), farmerId);
    }

    @PostMapping("/consents/{consentId}/revoke")
    ConsentDtos.Response revoke(@PathVariable UUID consentId,
                                @Valid @RequestBody ConsentDtos.RevokeRequest body,
                                HttpServletRequest request) {
        return service.revoke(tenants.resolve(request).tenantId(),
                actorId(), consentId, body);
    }

    @PostMapping("/consent-checks")
    ConsentDtos.CheckResponse check(@Valid @RequestBody ConsentDtos.CheckRequest body,
                                    HttpServletRequest request) {
        return service.check(tenants.resolve(request).tenantId(), body);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
