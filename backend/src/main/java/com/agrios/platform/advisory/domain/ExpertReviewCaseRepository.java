package com.agrios.platform.advisory.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpertReviewCaseRepository extends JpaRepository<ExpertReviewCaseEntity, UUID> {
    Optional<ExpertReviewCaseEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ExpertReviewCaseEntity> findByTenantIdAndAssignedExpertIdAndStatusInOrderByDueAtAsc(
            UUID tenantId, UUID expertId, Collection<String> statuses);
}
