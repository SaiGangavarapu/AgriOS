package com.agrios.platform.knowledge.api;

import com.agrios.platform.common.api.PageResponse;
import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.knowledge.application.KnowledgeService;
import com.agrios.platform.knowledge.domain.KnowledgeStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {
    private final KnowledgeService service;
    private final TenantContextResolver tenants;

    public KnowledgeController(KnowledgeService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/crops")
    KnowledgeDtos.CropResponse createCrop(
            @Valid @RequestBody KnowledgeDtos.CropRequest body,
            HttpServletRequest request) {
        return service.createCrop(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @GetMapping("/crops")
    PageResponse<KnowledgeDtos.CropResponse> listCrops(
            @RequestParam(required = false) KnowledgeStatus status,
            @PageableDefault(size = 20, sort = "defaultName") Pageable pageable,
            HttpServletRequest request) {
        return PageResponse.from(service.listCrops(
                tenants.resolve(request).tenantId(), status, pageable));
    }

    @PostMapping("/crops/{cropId}/submit-review")
    KnowledgeDtos.CropResponse submitReview(
            @PathVariable UUID cropId, HttpServletRequest request) {
        return service.submitReview(tenants.resolve(request).tenantId(), actorId(), cropId);
    }

    @PostMapping("/crops/{cropId}/approve")
    KnowledgeDtos.CropResponse approve(
            @PathVariable UUID cropId, HttpServletRequest request) {
        return service.approve(tenants.resolve(request).tenantId(), actorId(), cropId);
    }

    @PostMapping("/crops/{cropId}/publish")
    KnowledgeDtos.CropResponse publish(
            @PathVariable UUID cropId, HttpServletRequest request) {
        return service.publish(tenants.resolve(request).tenantId(), actorId(), cropId);
    }

    @PostMapping("/varieties")
    KnowledgeDtos.VarietyResponse createVariety(
            @Valid @RequestBody KnowledgeDtos.VarietyRequest body,
            HttpServletRequest request) {
        return service.createVariety(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @GetMapping("/crops/{cropId}/varieties")
    List<KnowledgeDtos.VarietyResponse> listVarieties(
            @PathVariable UUID cropId, HttpServletRequest request) {
        return service.listVarieties(tenants.resolve(request).tenantId(), cropId);
    }

    @PostMapping("/crops/{cropId}/requirements")
    KnowledgeDtos.RequirementResponse addRequirement(
            @PathVariable UUID cropId,
            @Valid @RequestBody KnowledgeDtos.RequirementRequest body,
            HttpServletRequest request) {
        return service.addRequirement(tenants.resolve(request).tenantId(), cropId, body);
    }

    @GetMapping("/crops/{cropId}/requirements")
    List<KnowledgeDtos.RequirementResponse> listRequirements(
            @PathVariable UUID cropId, HttpServletRequest request) {
        return service.listRequirements(tenants.resolve(request).tenantId(), cropId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
