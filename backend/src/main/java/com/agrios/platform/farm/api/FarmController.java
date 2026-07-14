package com.agrios.platform.farm.api;

import com.agrios.platform.common.api.PageResponse;
import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.farm.application.FarmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FarmController {
    private final FarmService service;
    private final TenantContextResolver tenants;

    public FarmController(FarmService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/farms")
    ResponseEntity<FarmDtos.FarmResponse> registerFarm(
            @Valid @RequestBody FarmDtos.RegisterFarmRequest body,
            HttpServletRequest request) {
        var response = service.registerFarm(tenants.resolve(request).tenantId(), actorId(), body);
        return ResponseEntity.created(URI.create("/api/v1/farms/" + response.id())).body(response);
    }

    @GetMapping("/farms/{farmId}")
    FarmDtos.FarmResponse getFarm(@PathVariable UUID farmId, HttpServletRequest request) {
        return service.getFarm(tenants.resolve(request).tenantId(), farmId);
    }

    @GetMapping("/farms")
    PageResponse<FarmDtos.FarmResponse> listFarms(
            @RequestParam(required = false) UUID farmerId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable, HttpServletRequest request) {
        return PageResponse.from(service.listFarms(
                tenants.resolve(request).tenantId(), farmerId, pageable));
    }

    @PostMapping("/farms/{farmId}/fields")
    ResponseEntity<FarmDtos.FieldResponse> registerField(
            @PathVariable UUID farmId,
            @Valid @RequestBody FarmDtos.RegisterFieldRequest body,
            HttpServletRequest request) {
        var response = service.registerField(
                tenants.resolve(request).tenantId(), actorId(), farmId, body);
        return ResponseEntity.created(URI.create("/api/v1/fields/" + response.id())).body(response);
    }

    @GetMapping("/fields/{fieldId}")
    FarmDtos.FieldResponse getField(@PathVariable UUID fieldId, HttpServletRequest request) {
        return service.getField(tenants.resolve(request).tenantId(), fieldId);
    }

    @GetMapping("/farms/{farmId}/fields")
    PageResponse<FarmDtos.FieldResponse> listFields(
            @PathVariable UUID farmId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable, HttpServletRequest request) {
        return PageResponse.from(service.listFields(
                tenants.resolve(request).tenantId(), farmId, pageable));
    }

    @PostMapping("/fields/{fieldId}/boundaries")
    FarmDtos.BoundaryResponse addBoundary(
            @PathVariable UUID fieldId,
            @Valid @RequestBody FarmDtos.BoundaryRequest body,
            HttpServletRequest request) {
        return service.addBoundary(tenants.resolve(request).tenantId(),
                actorId(), fieldId, body);
    }

    @GetMapping("/fields/{fieldId}/boundaries")
    List<FarmDtos.BoundaryResponse> boundaryHistory(
            @PathVariable UUID fieldId, HttpServletRequest request) {
        return service.boundaryHistory(tenants.resolve(request).tenantId(), fieldId);
    }

    @PostMapping("/farms/{farmId}/water-sources")
    FarmDtos.WaterSourceResponse addWaterSource(
            @PathVariable UUID farmId,
            @Valid @RequestBody FarmDtos.WaterSourceRequest body,
            HttpServletRequest request) {
        return service.addWaterSource(tenants.resolve(request).tenantId(),
                actorId(), farmId, body);
    }

    @GetMapping("/farms/{farmId}/water-sources")
    List<FarmDtos.WaterSourceResponse> listWaterSources(
            @PathVariable UUID farmId, HttpServletRequest request) {
        return service.listWaterSources(tenants.resolve(request).tenantId(), farmId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
