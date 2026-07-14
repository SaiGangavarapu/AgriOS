package com.agrios.platform.tenure.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farm.domain.FieldRepository;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.tenure.api.TenureDtos;
import com.agrios.platform.tenure.domain.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenureService {
    private final TenureRepository repository;
    private final FieldRepository fields;
    private final FarmerRepository farmers;

    public TenureService(TenureRepository repository, FieldRepository fields,
                         FarmerRepository farmers) {
        this.repository = repository;
        this.fields = fields;
        this.farmers = farmers;
    }

    @Transactional
    public TenureDtos.Response create(UUID tenantId, UUID actorId, UUID fieldId,
                                      TenureDtos.CreateRequest request) {
        fields.findByIdAndTenantId(fieldId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        farmers.findByIdAndTenantId(request.cultivatorFarmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Cultivator farmer not found."));

        LocalDate end = request.validTo() == null ? LocalDate.of(9999, 12, 31) : request.validTo();
        if (repository.existsByTenantIdAndFieldIdAndStatusAndValidFromLessThanEqualAndValidToGreaterThanEqual(
                tenantId, fieldId, "ACTIVE", end, request.validFrom())) {
            throw new ConflictException("TENURE_CONFLICT_REVIEW_REQUIRED",
                    "An active tenure arrangement overlaps this period.");
        }

        return TenureDtos.Response.from(repository.save(TenureEntity.create(
                tenantId, fieldId, request.tenureType(),
                request.cultivatorFarmerId(), request.validFrom(),
                request.validTo(), request.evidenceReference(), actorId)));
    }

    @Transactional(readOnly = true)
    public List<TenureDtos.Response> list(UUID tenantId, UUID fieldId) {
        fields.findByIdAndTenantId(fieldId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        return repository.findByTenantIdAndFieldIdOrderByValidFromDesc(tenantId, fieldId)
                .stream().map(TenureDtos.Response::from).toList();
    }

    @Transactional
    public TenureDtos.Response end(UUID tenantId, UUID actorId, UUID id,
                                   TenureDtos.EndRequest request) {
        TenureEntity value = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TENURE_NOT_FOUND", "Tenure arrangement not found."));
        value.end(request.endDate(), actorId);
        return TenureDtos.Response.from(value);
    }
}
