package com.agrios.platform.soilwater.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.soilwater.api.SoilWaterDtos;
import com.agrios.platform.soilwater.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoilWaterService {
    private final LaboratoryRepository laboratories;
    private final SampleRepository samples;
    private final TestReportRepository reports;
    private final ProfileRepository profiles;
    private final FieldRepository fields;
    private final WaterSourceRepository waterSources;
    private final ObjectMapper mapper;

    public SoilWaterService(LaboratoryRepository laboratories,
                            SampleRepository samples,
                            TestReportRepository reports,
                            ProfileRepository profiles,
                            FieldRepository fields,
                            WaterSourceRepository waterSources,
                            ObjectMapper mapper) {
        this.laboratories = laboratories;
        this.samples = samples;
        this.reports = reports;
        this.profiles = profiles;
        this.fields = fields;
        this.waterSources = waterSources;
        this.mapper = mapper;
    }

    @Transactional
    public SoilWaterDtos.LaboratoryResponse createLaboratory(
            UUID tenantId, UUID actorId, SoilWaterDtos.LaboratoryRequest request) {
        return SoilWaterDtos.LaboratoryResponse.from(laboratories.save(
                LaboratoryEntity.create(tenantId, request.code(), request.name(),
                        request.laboratoryType(), request.accreditationReference(), actorId)));
    }

    @Transactional(readOnly = true)
    public List<SoilWaterDtos.LaboratoryResponse> listLaboratories(UUID tenantId) {
        return laboratories.findByTenantIdAndStatusOrderByName(tenantId, "ACTIVE")
                .stream().map(SoilWaterDtos.LaboratoryResponse::from).toList();
    }

    @Transactional
    public SoilWaterDtos.SampleResponse collectSoilSample(
            UUID tenantId, UUID actorId, SoilWaterDtos.SoilSampleRequest request) {
        fields.findByIdAndTenantId(request.fieldId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
        return SoilWaterDtos.SampleResponse.from(samples.save(SampleEntity.soil(
                tenantId, request.fieldId(), request.sampleCode(),
                request.collectedAt(), actorId, request.collectionDepthCm(),
                request.collectionMethod(), request.recentInputNotes())));
    }

    @Transactional
    public SoilWaterDtos.SampleResponse collectWaterSample(
            UUID tenantId, UUID actorId, SoilWaterDtos.WaterSampleRequest request) {
        WaterSourceEntity waterSource = waterSources.findByIdAndTenantId(
                        request.waterSourceId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WATER_SOURCE_NOT_FOUND", "Water source not found."));
        return SoilWaterDtos.SampleResponse.from(samples.save(SampleEntity.water(
                tenantId, waterSource.id(), request.sampleCode(),
                request.collectedAt(), actorId, request.collectionMethod(),
                request.recentInputNotes())));
    }

    @Transactional
    public SoilWaterDtos.TestReportResponse createReport(
            UUID tenantId, UUID actorId, SoilWaterDtos.TestReportRequest request) {
        SampleEntity sample = requireSample(tenantId, request.sampleId());
        laboratories.findByIdAndTenantId(request.laboratoryId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LABORATORY_NOT_FOUND", "Laboratory not found."));
        TestReportEntity report = TestReportEntity.create(
                tenantId, sample.id(), request.laboratoryId(),
                request.reportNumber(), request.testedAt(),
                request.interpretationRuleVersion(), request.validUntil(),
                request.notes(), actorId);
        request.results().forEach(r -> report.addResult(
                r.parameterCode(), r.value(), r.unitCode(),
                r.analyticalMethod(), r.qualityFlag(),
                r.referenceMin(), r.referenceMax()));
        return SoilWaterDtos.TestReportResponse.from(reports.save(report));
    }

    @Transactional
    public SoilWaterDtos.ProfileResponse publishReport(
            UUID tenantId, UUID actorId, UUID reportId) {
        TestReportEntity report = reports.findByIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TEST_REPORT_NOT_FOUND", "Test report not found."));
        SampleEntity sample = requireSample(tenantId, report.sampleId());
        report.publish(actorId);
        sample.markPublished(actorId);

        if (sample.sampleType() == SampleType.SOIL) {
            profiles.findByTenantIdAndFieldIdAndProfileTypeAndIsCurrentTrue(
                    tenantId, sample.fieldId(), "SOIL").ifPresent(ProfileEntity::retire);
        } else {
            profiles.findByTenantIdAndWaterSourceIdAndProfileTypeAndIsCurrentTrue(
                    tenantId, sample.waterSourceId(), "WATER").ifPresent(ProfileEntity::retire);
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        List<String> constraints = new ArrayList<>();
        for (TestResultEntity result : report.results()) {
            summary.put(result.parameterCode(), Map.of(
                    "value", result.value(),
                    "unit", result.unitCode(),
                    "quality", result.qualityFlag()));
            if (result.referenceMin() != null && result.value().compareTo(result.referenceMin()) < 0) {
                constraints.add(result.parameterCode() + "_LOW");
            }
            if (result.referenceMax() != null && result.value().compareTo(result.referenceMax()) > 0) {
                constraints.add(result.parameterCode() + "_HIGH");
            }
        }

        ProfileEntity profile = ProfileEntity.create(
                tenantId, sample, report.id(), json(summary),
                json(constraints), report.validUntil());
        return SoilWaterDtos.ProfileResponse.from(profiles.save(profile));
    }

    @Transactional(readOnly = true)
    public SoilWaterDtos.ProfileResponse currentSoilProfile(UUID tenantId, UUID fieldId) {
        return profiles.findByTenantIdAndFieldIdAndProfileTypeAndIsCurrentTrue(
                        tenantId, fieldId, "SOIL")
                .map(SoilWaterDtos.ProfileResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SOIL_PROFILE_NOT_FOUND", "Current soil profile not found."));
    }

    @Transactional(readOnly = true)
    public SoilWaterDtos.ProfileResponse currentWaterProfile(UUID tenantId, UUID waterSourceId) {
        return profiles.findByTenantIdAndWaterSourceIdAndProfileTypeAndIsCurrentTrue(
                        tenantId, waterSourceId, "WATER")
                .map(SoilWaterDtos.ProfileResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WATER_PROFILE_NOT_FOUND", "Current water profile not found."));
    }

    private SampleEntity requireSample(UUID tenantId, UUID sampleId) {
        return samples.findByIdAndTenantId(sampleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SAMPLE_NOT_FOUND", "Sample not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize profile data.", 500);
        }
    }
}
