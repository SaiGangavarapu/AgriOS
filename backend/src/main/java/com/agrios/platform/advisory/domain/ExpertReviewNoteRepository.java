package com.agrios.platform.advisory.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpertReviewNoteRepository extends JpaRepository<ExpertReviewNoteEntity, UUID> {}
