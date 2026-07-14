package com.agrios.platform.advisory.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpertReviewDecisionRepository extends JpaRepository<ExpertReviewDecisionEntity, UUID> {
    boolean existsByReviewCaseId(UUID reviewCaseId);
}
