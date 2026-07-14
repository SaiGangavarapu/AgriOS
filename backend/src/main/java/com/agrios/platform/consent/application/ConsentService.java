package com.agrios.platform.consent.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.consent.api.ConsentDtos;
import com.agrios.platform.consent.domain.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsentService {
    private final ConsentRepository repository;
    private final FarmerRepository farmers;
    private final ObjectMapper mapper;

    public ConsentService(ConsentRepository repository,
                          FarmerRepository farmers,
                          ObjectMapper mapper) {
        this.repository = repository;
        this.farmers = farmers;
        this.mapper = mapper;
    }

    @Transactional
    public ConsentDtos.Response grant(UUID tenantId, UUID actorId,
                                      ConsentDtos.GrantRequest request) {
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
        ConsentEntity entity = ConsentEntity.grant(tenantId, request.farmerId(),
                request.purposeCode(), request.recipientType(),
                request.recipientId(), json(request.dataCategories()),
                json(request.scope()), request.policyVersion(),
                request.language(), request.validFrom(),
                request.validUntil(), actorId);
        return ConsentDtos.Response.from(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ConsentDtos.Response> list(UUID tenantId, UUID farmerId) {
        return repository.findByTenantIdAndFarmerIdOrderByCreatedAtDesc(tenantId, farmerId)
                .stream().map(ConsentDtos.Response::from).toList();
    }

    @Transactional
    public ConsentDtos.Response revoke(UUID tenantId, UUID actorId, UUID consentId,
                                       ConsentDtos.RevokeRequest request) {
        ConsentEntity entity = repository.findById(consentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CONSENT_NOT_FOUND", "Consent not found."));
        entity.revoke(request.reason(), actorId);
        return ConsentDtos.Response.from(entity);
    }

    @Transactional(readOnly = true)
    public ConsentDtos.CheckResponse check(UUID tenantId, ConsentDtos.CheckRequest request) {
        Optional<ConsentEntity> consent = repository
                .findByTenantIdAndFarmerIdAndPurposeCodeAndRecipientTypeAndRecipientIdAndStatus(
                        tenantId, request.farmerId(), request.purposeCode(),
                        request.recipientType(), request.recipientId(), "ACTIVE")
                .stream().filter(c -> c.activeAt(Instant.now())).findFirst();
        return consent.map(c -> new ConsentDtos.CheckResponse(true, c.id(), "ACTIVE_CONSENT"))
                .orElseGet(() -> new ConsentDtos.CheckResponse(false, null, "CONSENT_REQUIRED"));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("REQUEST_SERIALIZATION_FAILED",
                    "Unable to serialize consent scope.", 400);
        }
    }
}
