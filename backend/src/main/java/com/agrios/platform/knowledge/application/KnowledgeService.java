package com.agrios.platform.knowledge.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.knowledge.api.KnowledgeDtos;
import com.agrios.platform.knowledge.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeService {
    private final CropRepository crops;
    private final VarietyRepository varieties;
    private final CropRequirementRepository requirements;
    private final ObjectMapper mapper;

    public KnowledgeService(CropRepository crops, VarietyRepository varieties,
                            CropRequirementRepository requirements, ObjectMapper mapper) {
        this.crops = crops;
        this.varieties = varieties;
        this.requirements = requirements;
        this.mapper = mapper;
    }

    @Transactional
    public KnowledgeDtos.CropResponse createCrop(
            UUID tenantId, UUID actorId, KnowledgeDtos.CropRequest request) {
        return KnowledgeDtos.CropResponse.from(crops.save(CropEntity.create(
                tenantId, request.code(), request.defaultName(),
                request.scientificName(), request.cropCategory(),
                request.durationMinDays(), request.durationMaxDays(),
                request.evidenceGrade(), actorId)));
    }

    @Transactional(readOnly = true)
    public Page<KnowledgeDtos.CropResponse> listCrops(
            UUID tenantId, KnowledgeStatus status, Pageable pageable) {
        Page<CropEntity> values = status == null
                ? crops.findByTenantId(tenantId, pageable)
                : crops.findByTenantIdAndStatus(tenantId, status, pageable);
        return values.map(KnowledgeDtos.CropResponse::from);
    }

    @Transactional
    public KnowledgeDtos.CropResponse submitReview(UUID tenantId, UUID actorId, UUID cropId) {
        CropEntity crop = requireCrop(tenantId, cropId);
        crop.submitReview(actorId);
        return KnowledgeDtos.CropResponse.from(crop);
    }

    @Transactional
    public KnowledgeDtos.CropResponse approve(UUID tenantId, UUID actorId, UUID cropId) {
        CropEntity crop = requireCrop(tenantId, cropId);
        crop.approve(actorId);
        return KnowledgeDtos.CropResponse.from(crop);
    }

    @Transactional
    public KnowledgeDtos.CropResponse publish(UUID tenantId, UUID actorId, UUID cropId) {
        CropEntity crop = requireCrop(tenantId, cropId);
        crop.publish(actorId);
        return KnowledgeDtos.CropResponse.from(crop);
    }

    @Transactional
    public KnowledgeDtos.VarietyResponse createVariety(
            UUID tenantId, UUID actorId, KnowledgeDtos.VarietyRequest request) {
        requireCrop(tenantId, request.cropId());
        VarietyEntity variety = VarietyEntity.create(
                tenantId, request.cropId(), request.code(), request.defaultName(),
                request.releaseStatus(), request.durationDays(),
                json(defaultSet(request.seasonCodes())),
                json(defaultSet(request.geographyCodes())),
                json(defaultSet(request.toleranceTraits())),
                json(defaultSet(request.resistanceTraits())),
                json(request.marketTraits() == null ? Map.of() : request.marketTraits()),
                actorId);
        return KnowledgeDtos.VarietyResponse.from(varieties.save(variety));
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDtos.VarietyResponse> listVarieties(UUID tenantId, UUID cropId) {
        requireCrop(tenantId, cropId);
        return varieties.findByTenantIdAndCropId(tenantId, cropId)
                .stream().map(KnowledgeDtos.VarietyResponse::from).toList();
    }

    @Transactional
    public KnowledgeDtos.RequirementResponse addRequirement(
            UUID tenantId, UUID cropId, KnowledgeDtos.RequirementRequest request) {
        requireCrop(tenantId, cropId);
        return KnowledgeDtos.RequirementResponse.from(requirements.save(
                CropRequirementEntity.create(cropId, request.requirementType(),
                        request.minimumValue(), request.maximumValue(),
                        request.unitCode(), request.hardConstraint(),
                        json(request.applicability() == null ? Map.of() : request.applicability()),
                        request.evidenceReference())));
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDtos.RequirementResponse> listRequirements(
            UUID tenantId, UUID cropId) {
        requireCrop(tenantId, cropId);
        return requirements.findByCropId(cropId).stream()
                .map(KnowledgeDtos.RequirementResponse::from).toList();
    }

    private CropEntity requireCrop(UUID tenantId, UUID cropId) {
        return crops.findByIdAndTenantId(cropId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_NOT_FOUND", "Crop not found."));
    }

    private Set<String> defaultSet(Set<String> value) {
        return value == null ? Set.of() : value;
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize knowledge data.", 500);
        }
    }
}
