package com.agrios.platform.advisory.application;

import com.agrios.platform.advisory.api.AdvisoryDtos;
import com.agrios.platform.advisory.domain.*;
import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdvisoryService {
    private final AdvisoryRepository advisories;
    private final AdvisoryActionRepository actions;
    private final FarmerRepository farmers;
    private final ObjectMapper mapper;

    public AdvisoryService(AdvisoryRepository advisories,
                           AdvisoryActionRepository actions,
                           FarmerRepository farmers,
                           ObjectMapper mapper) {
        this.advisories = advisories;
        this.actions = actions;
        this.farmers = farmers;
        this.mapper = mapper;
    }

    @Transactional
    public AdvisoryDtos.Response create(UUID tenantId, UUID actorId,
                                        AdvisoryDtos.CreateRequest request) {
        if (request.farmerId() != null) {
            farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARMER_NOT_FOUND", "Farmer not found."));
        }
        AdvisoryEntity advisory = advisories.save(AdvisoryEntity.create(
                tenantId, request.farmerId(), request.farmId(),
                request.fieldId(), request.cropCycleId(),
                request.advisoryType(), request.priority(),
                request.title(), request.summary(),
                request.detailedGuidance(), request.sourceType(),
                request.sourceReferenceId(), request.language(),
                request.validFrom(), request.validUntil(),
                request.confidenceScore(),
                json(request.reasonCodes() == null ? List.of() : request.reasonCodes()),
                json(request.evidenceSnapshot() == null ? Map.of() : request.evidenceSnapshot()),
                actorId));

        if (request.actions() != null) {
            request.actions().forEach(a -> actions.save(AdvisoryActionEntity.create(
                    advisory.id(), a.actionCode(), a.actionLabel(),
                    a.actionDescription(), a.dueAt(),
                    a.actionPriority(), a.taskTemplateType())));
        }
        return AdvisoryDtos.Response.from(advisory);
    }

    @Transactional
    public AdvisoryDtos.Response submit(UUID tenantId, UUID actorId, UUID advisoryId) {
        AdvisoryEntity advisory = requireAdvisory(tenantId, advisoryId);
        advisory.submitForReview(actorId);
        return AdvisoryDtos.Response.from(advisory);
    }

    @Transactional
    public AdvisoryDtos.Response approve(UUID tenantId, UUID actorId, UUID advisoryId) {
        AdvisoryEntity advisory = requireAdvisory(tenantId, advisoryId);
        advisory.approve(actorId);
        return AdvisoryDtos.Response.from(advisory);
    }

    @Transactional
    public AdvisoryDtos.Response publish(UUID tenantId, UUID actorId, UUID advisoryId) {
        AdvisoryEntity advisory = requireAdvisory(tenantId, advisoryId);
        advisory.publish(actorId);
        return AdvisoryDtos.Response.from(advisory);
    }

    @Transactional
    public AdvisoryDtos.Response withdraw(UUID tenantId, UUID actorId, UUID advisoryId) {
        AdvisoryEntity advisory = requireAdvisory(tenantId, advisoryId);
        advisory.withdraw(actorId);
        return AdvisoryDtos.Response.from(advisory);
    }

    @Transactional(readOnly = true)
    public List<AdvisoryDtos.Response> publishedForFarmer(
            UUID tenantId, UUID farmerId) {
        farmers.findByIdAndTenantId(farmerId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        Instant now = Instant.now();
        return advisories.findByTenantIdAndFarmerIdAndStatusOrderByValidFromDesc(
                        tenantId, farmerId, "PUBLISHED")
                .stream()
                .filter(a -> !a.validFrom().isAfter(now))
                .filter(a -> a.validUntil() == null || a.validUntil().isAfter(now))
                .map(AdvisoryDtos.Response::from)
                .toList();
    }

    private AdvisoryEntity requireAdvisory(UUID tenantId, UUID advisoryId) {
        return advisories.findByIdAndTenantId(advisoryId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ADVISORY_NOT_FOUND", "Advisory not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize advisory data.", 500);
        }
    }
}
