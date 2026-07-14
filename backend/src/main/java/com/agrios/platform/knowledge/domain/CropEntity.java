package com.agrios.platform.knowledge.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "crop", schema = "knowledge")
public class CropEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false, length = 80) private String code;
    private String scientificName;
    @Column(nullable = false) private String defaultName;
    @Column(nullable = false) private String cropCategory;
    private Integer durationMinDays;
    private Integer durationMaxDays;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private KnowledgeStatus status;
    @Column(nullable = false) private String evidenceGrade;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected CropEntity() {}

    public static CropEntity create(UUID tenantId, String code, String name,
                                    String scientificName, String category,
                                    Integer minDays, Integer maxDays,
                                    String evidenceGrade, UUID actorId) {
        CropEntity crop = new CropEntity();
        crop.id = UUID.randomUUID();
        crop.tenantId = tenantId;
        crop.code = code.trim().toUpperCase(Locale.ROOT);
        crop.defaultName = name.trim();
        crop.scientificName = scientificName;
        crop.cropCategory = category;
        crop.durationMinDays = minDays;
        crop.durationMaxDays = maxDays;
        crop.status = KnowledgeStatus.DRAFT;
        crop.evidenceGrade = evidenceGrade;
        crop.createdAt = Instant.now();
        crop.updatedAt = crop.createdAt;
        crop.createdBy = actorId;
        crop.updatedBy = actorId;
        return crop;
    }

    public void submitReview(UUID actorId) { transition(KnowledgeStatus.DRAFT, KnowledgeStatus.IN_REVIEW, actorId); }
    public void approve(UUID actorId) { transition(KnowledgeStatus.IN_REVIEW, KnowledgeStatus.APPROVED, actorId); }
    public void publish(UUID actorId) { transition(KnowledgeStatus.APPROVED, KnowledgeStatus.PUBLISHED, actorId); }

    private void transition(KnowledgeStatus expected, KnowledgeStatus next, UUID actorId) {
        if (status != expected) throw new IllegalStateException("Expected status " + expected);
        status = next;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public String code() { return code; }
    public String scientificName() { return scientificName; }
    public String defaultName() { return defaultName; }
    public String cropCategory() { return cropCategory; }
    public Integer durationMinDays() { return durationMinDays; }
    public Integer durationMaxDays() { return durationMaxDays; }
    public KnowledgeStatus status() { return status; }
    public String evidenceGrade() { return evidenceGrade; }
    public long version() { return version; }
    public Instant createdAt() { return createdAt; }
}
