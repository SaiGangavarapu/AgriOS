package com.agrios.platform.advisory.application;

import com.agrios.platform.advisory.api.ExpertReviewDtos;
import com.agrios.platform.advisory.domain.*;
import com.agrios.platform.common.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpertReviewService {
    private static final Set<String> ACTIVE =
            Set.of("OPEN","ASSIGNED","IN_REVIEW","MORE_INFORMATION_REQUIRED");

    private final ExpertProfileRepository experts;
    private final ExpertReviewCaseRepository cases;
    private final ExpertReviewNoteRepository notes;
    private final ExpertReviewDecisionRepository decisions;
    private final ObjectMapper mapper;

    public ExpertReviewService(ExpertProfileRepository experts,
                               ExpertReviewCaseRepository cases,
                               ExpertReviewNoteRepository notes,
                               ExpertReviewDecisionRepository decisions,
                               ObjectMapper mapper) {
        this.experts = experts;
        this.cases = cases;
        this.notes = notes;
        this.decisions = decisions;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createExpert(UUID tenantId,
                             ExpertReviewDtos.ExpertProfileRequest request) {
        return experts.save(ExpertProfileEntity.create(
                tenantId, request.userAccountId(), request.displayName(),
                request.expertType(), request.organizationName(),
                request.qualificationSummary(),
                request.registrationReference(),
                json(request.geographyCodes() == null ? Set.of() : request.geographyCodes()),
                json(request.cropCodes() == null ? Set.of() : request.cropCodes()),
                json(request.languageCodes() == null ? Set.of() : request.languageCodes())
        )).id();
    }

    @Transactional
    public ExpertReviewDtos.CaseResponse createCase(
            UUID tenantId, UUID actorId,
            ExpertReviewDtos.CreateCaseRequest request) {
        ExpertReviewCaseEntity value = ExpertReviewCaseEntity.create(
                tenantId, request.caseType(), request.subjectType(),
                request.subjectId(), request.farmerId(), request.fieldId(),
                request.cropCycleId(), actorId, request.priority(),
                request.dueAt(), request.question(),
                json(request.contextSnapshot() == null ? Map.of() : request.contextSnapshot()));
        return ExpertReviewDtos.CaseResponse.from(cases.save(value));
    }

    @Transactional
    public ExpertReviewDtos.CaseResponse assign(
            UUID tenantId, UUID caseId, UUID expertId) {
        ExpertProfileEntity expert = experts.findByIdAndTenantId(expertId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EXPERT_NOT_FOUND", "Expert not found."));
        if (!"ACTIVE".equals(expert.status())) {
            throw new BusinessException("EXPERT_NOT_ACTIVE",
                    "Expert is not active.", 422);
        }
        ExpertReviewCaseEntity reviewCase = requireCase(tenantId, caseId);
        reviewCase.assign(expert.id());
        return ExpertReviewDtos.CaseResponse.from(reviewCase);
    }

    @Transactional
    public ExpertReviewDtos.CaseResponse start(UUID tenantId, UUID caseId) {
        ExpertReviewCaseEntity reviewCase = requireCase(tenantId, caseId);
        reviewCase.startReview();
        return ExpertReviewDtos.CaseResponse.from(reviewCase);
    }

    @Transactional
    public void addNote(UUID tenantId, UUID caseId,
                        ExpertReviewDtos.NoteRequest request) {
        ExpertReviewCaseEntity reviewCase = requireCase(tenantId, caseId);
        if (reviewCase.assignedExpertId() == null ||
                !reviewCase.assignedExpertId().equals(request.expertId())) {
            throw new BusinessException("EXPERT_CASE_ASSIGNMENT_MISMATCH",
                    "Expert is not assigned to this case.", 422);
        }
        notes.save(ExpertReviewNoteEntity.create(
                caseId, request.expertId(), request.noteType(),
                request.noteText(),
                json(request.evidenceReferences() == null
                        ? List.of() : request.evidenceReferences())));
    }

    @Transactional
    public ExpertReviewDtos.CaseResponse decide(
            UUID tenantId, UUID caseId,
            ExpertReviewDtos.DecisionRequest request) {
        ExpertReviewCaseEntity reviewCase = requireCase(tenantId, caseId);
        if (reviewCase.assignedExpertId() == null ||
                !reviewCase.assignedExpertId().equals(request.expertId())) {
            throw new BusinessException("EXPERT_CASE_ASSIGNMENT_MISMATCH",
                    "Expert is not assigned to this case.", 422);
        }
        if (decisions.existsByReviewCaseId(caseId)) {
            throw new ConflictException("EXPERT_DECISION_ALREADY_EXISTS",
                    "Decision already exists for this review case.");
        }
        decisions.save(ExpertReviewDecisionEntity.create(
                caseId, request.expertId(), request.decisionCode(),
                request.decisionSummary(), request.recommendationText(),
                request.riskLevel(), request.followUpRequired(),
                request.followUpDueAt(), request.advisoryId()));
        reviewCase.complete();
        return ExpertReviewDtos.CaseResponse.from(reviewCase);
    }

    @Transactional(readOnly = true)
    public List<ExpertReviewDtos.CaseResponse> workQueue(
            UUID tenantId, UUID expertId) {
        return cases.findByTenantIdAndAssignedExpertIdAndStatusInOrderByDueAtAsc(
                        tenantId, expertId, ACTIVE)
                .stream().map(ExpertReviewDtos.CaseResponse::from).toList();
    }

    private ExpertReviewCaseEntity requireCase(UUID tenantId, UUID caseId) {
        return cases.findByIdAndTenantId(caseId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EXPERT_REVIEW_CASE_NOT_FOUND", "Expert review case not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize expert review data.", 500);
        }
    }
}
