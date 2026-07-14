package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "expert_review_note", schema = "advisory")
public class ExpertReviewNoteEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID reviewCaseId;
    @Column(nullable = false) private UUID expertId;
    @Column(nullable = false) private String noteType;
    @Column(nullable = false, columnDefinition = "text") private String noteText;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceReferences;
    @Column(nullable = false) private Instant createdAt;

    protected ExpertReviewNoteEntity() {}

    public static ExpertReviewNoteEntity create(
            UUID reviewCaseId, UUID expertId, String noteType,
            String noteText, String evidenceReferences) {
        ExpertReviewNoteEntity value = new ExpertReviewNoteEntity();
        value.id = UUID.randomUUID();
        value.reviewCaseId = reviewCaseId;
        value.expertId = expertId;
        value.noteType = noteType;
        value.noteText = noteText;
        value.evidenceReferences = evidenceReferences;
        value.createdAt = Instant.now();
        return value;
    }
}
