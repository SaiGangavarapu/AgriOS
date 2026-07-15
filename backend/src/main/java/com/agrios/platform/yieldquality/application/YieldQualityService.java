package com.agrios.platform.yieldquality.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.harvest.domain.*;
import com.agrios.platform.yieldquality.api.YieldQualityDtos;
import com.agrios.platform.yieldquality.domain.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class YieldQualityService {
    private final HarvestBatchRepository batches;
    private final YieldRecordRepository yields;
    private final QualityParameterRepository parameters;
    private final QualityAssessmentRepository assessments;
    private final QualityResultRepository results;

    public YieldQualityService(HarvestBatchRepository batches,
                               YieldRecordRepository yields,
                               QualityParameterRepository parameters,
                               QualityAssessmentRepository assessments,
                               QualityResultRepository results) {
        this.batches = batches;
        this.yields = yields;
        this.parameters = parameters;
        this.assessments = assessments;
        this.results = results;
    }

    @Transactional
    public UUID recordYield(UUID tenantId, YieldQualityDtos.YieldRequest request) {
        HarvestBatchEntity batch = batches.findByIdAndTenantId(
                        request.harvestBatchId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HARVEST_BATCH_NOT_FOUND", "Harvest batch not found."));
        if (yields.existsByHarvestBatchId(batch.id())) {
            throw new ConflictException("YIELD_ALREADY_RECORDED",
                    "Yield record already exists for this batch.");
        }
        return yields.save(YieldRecordEntity.create(
                tenantId, batch.cropCycleId(), batch.fieldId(),
                batch.id(), batch.netQuantity(), batch.quantityUnit(),
                request.fieldAreaHectares(), request.expectedYieldPerHectare(),
                request.marketableQuantity(), request.rejectedQuantity())).id();
    }

    @Transactional
    public UUID createParameter(UUID tenantId,
                                YieldQualityDtos.ParameterRequest request) {
        return parameters.save(QualityParameterEntity.create(
                tenantId, request.cropId(), request.parameterCode(),
                request.parameterName(), request.valueType(),
                request.unitCode(), request.minimumValue(),
                request.maximumValue(), request.required())).id();
    }

    @Transactional
    public UUID createAssessment(UUID tenantId,
                                 YieldQualityDtos.AssessmentRequest request) {
        HarvestBatchEntity batch = batches.findByIdAndTenantId(
                        request.harvestBatchId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HARVEST_BATCH_NOT_FOUND", "Harvest batch not found."));
        QualityAssessmentEntity assessment = assessments.save(
                QualityAssessmentEntity.create(
                        tenantId, batch.id(), request.assessedAt(),
                        request.assessorType(), request.assessorId(),
                        request.notes()));
        batch.moveToQualityCheck(UUID.nameUUIDFromBytes("system".getBytes()));

        for (YieldQualityDtos.ResultRequest result : request.results()) {
            QualityParameterEntity parameter = parameters.findByIdAndTenantId(
                            result.qualityParameterId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QUALITY_PARAMETER_NOT_FOUND", "Quality parameter not found."));
            Boolean within = null;
            if (result.numericValue() != null) {
                within = (parameter.minimumValue() == null ||
                        result.numericValue().compareTo(parameter.minimumValue()) >= 0)
                        && (parameter.maximumValue() == null ||
                        result.numericValue().compareTo(parameter.maximumValue()) <= 0);
            }
            results.save(QualityResultEntity.create(
                    assessment.id(), parameter.id(),
                    result.numericValue(), result.textValue(),
                    result.booleanValue(), within, result.notes()));
        }
        return assessment.id();
    }

    @Transactional
    public void completeAssessment(UUID tenantId, UUID assessmentId,
                                   YieldQualityDtos.CompleteAssessmentRequest request) {
        QualityAssessmentEntity assessment = assessments
                .findByIdAndTenantId(assessmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "QUALITY_ASSESSMENT_NOT_FOUND", "Quality assessment not found."));
        assessment.complete(request.assignedGrade(),
                request.marketabilityStatus());
        batches.findByIdAndTenantId(assessment.harvestBatchId(), tenantId)
                .ifPresent(batch -> batch.markGraded(
                        UUID.nameUUIDFromBytes("system".getBytes())));
    }
}
