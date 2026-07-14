package com.agrios.platform.advisory.api;

import com.agrios.platform.advisory.application.ExpertReviewService;
import com.agrios.platform.common.web.TenantContextResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ExpertReviewController {
    private final ExpertReviewService service;
    private final TenantContextResolver tenants;

    public ExpertReviewController(ExpertReviewService service,
                                  TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/experts")
    Map<String, UUID> createExpert(
            @Valid @RequestBody ExpertReviewDtos.ExpertProfileRequest body,
            HttpServletRequest request) {
        return Map.of("expertId", service.createExpert(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/expert-review-cases")
    ExpertReviewDtos.CaseResponse createCase(
            @Valid @RequestBody ExpertReviewDtos.CreateCaseRequest body,
            HttpServletRequest request) {
        return service.createCase(
                tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/expert-review-cases/{caseId}/assign")
    ExpertReviewDtos.CaseResponse assign(
            @PathVariable UUID caseId,
            @Valid @RequestBody ExpertReviewDtos.AssignRequest body,
            HttpServletRequest request) {
        return service.assign(
                tenants.resolve(request).tenantId(), caseId, body.expertId());
    }

    @PostMapping("/expert-review-cases/{caseId}/start")
    ExpertReviewDtos.CaseResponse start(
            @PathVariable UUID caseId, HttpServletRequest request) {
        return service.start(tenants.resolve(request).tenantId(), caseId);
    }

    @PostMapping("/expert-review-cases/{caseId}/notes")
    void note(
            @PathVariable UUID caseId,
            @Valid @RequestBody ExpertReviewDtos.NoteRequest body,
            HttpServletRequest request) {
        service.addNote(tenants.resolve(request).tenantId(), caseId, body);
    }

    @PostMapping("/expert-review-cases/{caseId}/decision")
    ExpertReviewDtos.CaseResponse decide(
            @PathVariable UUID caseId,
            @Valid @RequestBody ExpertReviewDtos.DecisionRequest body,
            HttpServletRequest request) {
        return service.decide(
                tenants.resolve(request).tenantId(), caseId, body);
    }

    @GetMapping("/experts/{expertId}/review-cases")
    List<ExpertReviewDtos.CaseResponse> queue(
            @PathVariable UUID expertId, HttpServletRequest request) {
        return service.workQueue(
                tenants.resolve(request).tenantId(), expertId);
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
