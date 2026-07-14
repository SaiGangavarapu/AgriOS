package com.agrios.platform.cropplanning.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "crop_plan", schema = "cropplanning")
public class CropPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    private UUID assessmentId;
    @Column(nullable = false) private String seasonCode;
    @Column(nullable = false) private String farmingSystem;
    private UUID selectedCropId;
    private UUID selectedVarietyId;
    @Column(nullable = false) private BigDecimal plannedAreaHectares;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String approvalNotes;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected CropPlanEntity() {}

    public static CropPlanEntity create(
            UUID tenantId, UUID fieldId, UUID assessmentId, String season,
            String system, UUID cropId, UUID varietyId,
            BigDecimal area, UUID actorId) {
        CropPlanEntity plan = new CropPlanEntity();
        plan.id = UUID.randomUUID();
        plan.tenantId = tenantId;
        plan.fieldId = fieldId;
        plan.assessmentId = assessmentId;
        plan.seasonCode = season;
        plan.farmingSystem = system;
        plan.selectedCropId = cropId;
        plan.selectedVarietyId = varietyId;
        plan.plannedAreaHectares = area;
        plan.status = "GENERATED";
        plan.createdAt = Instant.now();
        plan.updatedAt = plan.createdAt;
        plan.createdBy = actorId;
        plan.updatedBy = actorId;
        return plan;
    }

    public void approve(String notes, UUID actorId) {
        if (selectedCropId == null) throw new IllegalStateException("Crop selection is required.");
        if (!status.equals("GENERATED") && !status.equals("FARMER_REVIEWED") &&
                !status.equals("EXPERT_REVIEW")) {
            throw new IllegalStateException("Crop plan cannot be approved from status " + status);
        }
        status = "APPROVED";
        approvalNotes = notes;
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public UUID assessmentId() { return assessmentId; }
    public String seasonCode() { return seasonCode; }
    public String farmingSystem() { return farmingSystem; }
    public UUID selectedCropId() { return selectedCropId; }
    public UUID selectedVarietyId() { return selectedVarietyId; }
    public BigDecimal plannedAreaHectares() { return plannedAreaHectares; }
    public String status() { return status; }
    public String approvalNotes() { return approvalNotes; }
    public Instant approvedAt() { return approvedAt; }
    public UUID approvedBy() { return approvedBy; }
    public long version() { return version; }
}
