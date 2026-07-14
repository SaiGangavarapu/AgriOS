package com.agrios.platform.tenure.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.tenure.application.TenureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TenureController {
    private final TenureService service;
    private final TenantContextResolver tenants;

    public TenureController(TenureService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/fields/{fieldId}/tenure-arrangements")
    TenureDtos.Response create(@PathVariable UUID fieldId,
                               @Valid @RequestBody TenureDtos.CreateRequest body,
                               HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(),
                actorId(), fieldId, body);
    }

    @GetMapping("/fields/{fieldId}/tenure-arrangements")
    List<TenureDtos.Response> list(@PathVariable UUID fieldId,
                                   HttpServletRequest request) {
        return service.list(tenants.resolve(request).tenantId(), fieldId);
    }

    @PostMapping("/tenure-arrangements/{id}/end")
    TenureDtos.Response end(@PathVariable UUID id,
                            @Valid @RequestBody TenureDtos.EndRequest body,
                            HttpServletRequest request) {
        return service.end(tenants.resolve(request).tenantId(), actorId(), id, body);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
