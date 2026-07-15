package com.agrios.platform.crophealth.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.crophealth.api.CropHealthDtos;
import com.agrios.platform.crophealth.domain.*;
import com.agrios.platform.farm.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropHealthService {
    private final PestCatalogRepository pests;
    private final DiseaseCatalogRepository diseases;
    private final ScoutingVisitRepository visits;
    private final ScoutingObservationRepository observations;
    private final CropHealthAssessmentRepository assessments;
    private final OutbreakEventRepository outbreaks;
    private final FieldRepository fields;
    private final CropCycleRepository cycles;
    private final ObjectMapper mapper;

    public CropHealthService(
            PestCatalogRepository pests,
            DiseaseCatalogRepository diseases,
            ScoutingVisitRepository visits,
            ScoutingObservationRepository observations,
            CropHealthAssessmentRepository assessments,
            OutbreakEventRepository outbreaks,
            FieldRepository fields,
            CropCycleRepository cycles,
            ObjectMapper mapper) {
        this.pests = pests;
        this.diseases = diseases;
        this.visits = visits;
        this.observations = observations;
        this.assessments = assessments;
        this.outbreaks = outbreaks;
        this.fields = fields;
        this.cycles = cycles;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createPest(UUID tenantId, CropHealthDtos.CatalogRequest request) {
        return pests.save(PestCatalogEntity.create(
                tenantId, request.code(), request.commonName(),
                request.scientificName(), request.category(),
                json(request.affectedCropCodes() == null ? Set.of() : request.affectedCropCodes()),
                json(request.symptoms() == null ? List.of() : request.symptoms()),
                json(request.favorableConditions() == null ? List.of() : request.favorableConditions()),
                request.notes(), request.evidenceGrade())).id();
    }

    @Transactional
    public UUID createDisease(UUID tenantId, CropHealthDtos.CatalogRequest request) {
        return diseases.save(DiseaseCatalogEntity.create(
                tenantId, request.code(), request.commonName(),
                request.scientificName(), request.category(), request.causalAgent(),
                json(request.affectedCropCodes() == null ? Set.of() : request.affectedCropCodes()),
                json(request.symptoms() == null ? List.of() : request.symptoms()),
                json(request.favorableConditions() == null ? List.of() : request.favorableConditions()),
                request.notes(), request.evidenceGrade())).id();
    }

    @Transactional
    public void publishPest(UUID tenantId, UUID pestId) {
        pests.findByIdAndTenantId(pestId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PEST_NOT_FOUND", "Pest not found.")).publish();
    }

    @Transactional
    public void publishDisease(UUID tenantId, UUID diseaseId) {
        diseases.findByIdAndTenantId(diseaseId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DISEASE_NOT_FOUND", "Disease not found.")).publish();
    }

    @Transactional
    public UUID scout(UUID tenantId, CropHealthDtos.ScoutingRequest request) {
        FieldEntity field = fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        if (!cycle.fieldId().equals(field.id())) {
            throw new ConflictException("SCOUTING_FIELD_CYCLE_MISMATCH",
                    "Crop cycle does not belong to the field.");
        }

        ScoutingVisitEntity visit = visits.save(ScoutingVisitEntity.create(
                tenantId, field.id(), cycle.id(), request.scoutedAt(),
                request.scoutType(), request.scoutId(),
                request.samplingMethod(), request.sampleAreaHectares(),
                request.plantCount(), request.notes(),
                json(request.weatherSnapshot() == null ? Map.of() : request.weatherSnapshot())));

        if (request.observations() != null) {
            for (CropHealthDtos.ObservationRequest o : request.observations()) {
                validateSubject(tenantId, o);
                observations.save(ScoutingObservationEntity.create(
                        visit.id(), o.observationType(), o.pestId(), o.diseaseId(),
                        o.observedSymptom(), o.affectedPlantCount(),
                        o.incidencePercent(), o.severityPercent(),
                        o.populationCount(), o.populationUnit(),
                        o.cropStageCode(), o.confidenceScore(),
                        json(o.evidence() == null ? Map.of() : o.evidence())));
            }
        }
        return visit.id();
    }

    @Transactional
    public CropHealthDtos.HealthAssessmentResponse assess(
            UUID tenantId, CropHealthDtos.HealthAssessmentRequest request) {
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));

        List<ScoutingVisitEntity> cycleVisits =
                visits.findByTenantIdAndCropCycleIdOrderByScoutedAtDesc(tenantId, cycle.id());

        BigDecimal pestPressure = BigDecimal.ZERO;
        BigDecimal diseasePressure = BigDecimal.ZERO;
        int evidenceCount = 0;

        for (ScoutingVisitEntity visit : cycleVisits) {
            for (ScoutingObservationEntity o : observations.findByScoutingVisitId(visit.id())) {
                BigDecimal score = o.severityPercent() != null
                        ? o.severityPercent()
                        : (o.incidencePercent() == null ? BigDecimal.ZERO : o.incidencePercent());
                if ("PEST".equals(o.observationType())) pestPressure = pestPressure.max(score);
                if ("DISEASE".equals(o.observationType())) diseasePressure = diseasePressure.max(score);
                evidenceCount++;
            }
        }

        BigDecimal pressure = pestPressure.max(diseasePressure);
        String status = pressure.compareTo(new BigDecimal("60")) >= 0 ? "CRITICAL"
                : pressure.compareTo(new BigDecimal("30")) >= 0 ? "AFFECTED"
                : pressure.compareTo(new BigDecimal("10")) >= 0 ? "AT_RISK"
                : evidenceCount == 0 ? "WATCH" : "HEALTHY";

        BigDecimal composite = BigDecimal.valueOf(100).subtract(pressure)
                .max(BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal confidence = evidenceCount == 0
                ? new BigDecimal("0.3000")
                : new BigDecimal("0.8000");

        List<String> reasons = new ArrayList<>();
        if (evidenceCount == 0) reasons.add("SCOUTING_DATA_MISSING");
        if (pestPressure.compareTo(new BigDecimal("30")) >= 0) reasons.add("HIGH_PEST_PRESSURE");
        if (diseasePressure.compareTo(new BigDecimal("30")) >= 0) reasons.add("HIGH_DISEASE_PRESSURE");

        CropHealthAssessmentEntity assessment =
                CropHealthAssessmentEntity.create(
                        tenantId, request.fieldId(), cycle.id(),
                        request.assessmentDate(), status,
                        null, null, null, pestPressure,
                        diseasePressure, null, null,
                        composite, confidence, json(reasons),
                        json(Map.of("scoutingVisitCount", cycleVisits.size(),
                                "observationCount", evidenceCount)));
        return CropHealthDtos.HealthAssessmentResponse.from(assessments.save(assessment));
    }

    @Transactional
    public UUID openOutbreak(UUID tenantId, CropHealthDtos.OutbreakRequest request) {
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));

        if ("PEST".equals(request.outbreakType())) {
            pests.findByIdAndTenantId(request.pestId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "PEST_NOT_FOUND", "Pest not found."));
        } else if ("DISEASE".equals(request.outbreakType())) {
            diseases.findByIdAndTenantId(request.diseaseId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "DISEASE_NOT_FOUND", "Disease not found."));
        } else {
            throw new BusinessException("OUTBREAK_TYPE_INVALID",
                    "Outbreak type must be PEST or DISEASE.", 422);
        }

        return outbreaks.save(OutbreakEventEntity.create(
                tenantId, request.fieldId(), request.cropCycleId(),
                request.pestId(), request.diseaseId(),
                request.outbreakType(), request.detectedAt(),
                request.severity(), request.affectedAreaHectares(),
                request.incidencePercent(), request.sourceObservationId())).id();
    }

    private void validateSubject(UUID tenantId, CropHealthDtos.ObservationRequest o) {
        if ("PEST".equals(o.observationType())) {
            if (o.pestId() == null) throw new BusinessException(
                    "PEST_REFERENCE_REQUIRED", "Pest ID is required.", 422);
            pests.findByIdAndTenantId(o.pestId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "PEST_NOT_FOUND", "Pest not found."));
        }
        if ("DISEASE".equals(o.observationType())) {
            if (o.diseaseId() == null) throw new BusinessException(
                    "DISEASE_REFERENCE_REQUIRED", "Disease ID is required.", 422);
            diseases.findByIdAndTenantId(o.diseaseId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "DISEASE_NOT_FOUND", "Disease not found."));
        }
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize crop-health data.", 500);
        }
    }
}
