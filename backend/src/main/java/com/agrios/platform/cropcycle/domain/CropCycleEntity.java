package com.agrios.platform.cropcycle.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "crop_cycle", schema = "cropcycle")
public class CropCycleEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropPlanId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private UUID cropId;
    private UUID varietyId;
    @Column(nullable = false) private String seasonCode;
    @Column(nullable = false) private BigDecimal plannedAreaHectares;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private CropCycleStatus status;
    private String currentStageCode;
    private Instant activatedAt;
    private LocalDate sowingDate;
    private Instant closedAt;
    @Column(columnDefinition = "text") private String closureNotes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected CropCycleEntity() {}

    public static CropCycleEntity create(UUID tenantId, UUID planId, UUID fieldId,
                                         UUID cropId, UUID varietyId, String season,
                                         BigDecimal area, UUID actorId) {
        CropCycleEntity value = new CropCycleEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropPlanId = planId;
        value.fieldId = fieldId;
        value.cropId = cropId;
        value.varietyId = varietyId;
        value.seasonCode = season;
        value.plannedAreaHectares = area;
        value.status = CropCycleStatus.PLANNED;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void activate(UUID actorId) {
        requireStatus(CropCycleStatus.PLANNED);
        status = CropCycleStatus.ACTIVATED;
        activatedAt = Instant.now();
        touch(actorId);
    }

    public void recordSowing(LocalDate date, UUID actorId) {
        if (status != CropCycleStatus.ACTIVATED &&
            status != CropCycleStatus.LAND_PREPARATION &&
            status != CropCycleStatus.RESOWING) {
            throw new IllegalStateException("Crop cycle is not ready for sowing.");
        }
        sowingDate = date;
        status = CropCycleStatus.SOWN;
        currentStageCode = "SOWN";
        touch(actorId);
    }

    public void updateStage(String stageCode, CropCycleStatus nextStatus, UUID actorId) {
        if (status == CropCycleStatus.CLOSED || status == CropCycleStatus.CANCELLED) {
            throw new IllegalStateException("Closed or cancelled cycle cannot change stage.");
        }
        currentStageCode = stageCode;
        status = nextStatus;
        touch(actorId);
    }

    public void registerLoss(boolean total, UUID actorId) {
        status = total ? CropCycleStatus.TOTAL_CROP_LOSS : CropCycleStatus.PARTIAL_CROP_LOSS;
        touch(actorId);
    }

    public void close(String notes, UUID actorId) {
        if (status == CropCycleStatus.CLOSED || status == CropCycleStatus.CANCELLED) {
            throw new IllegalStateException("Crop cycle is already terminal.");
        }
        status = CropCycleStatus.CLOSED;
        closedAt = Instant.now();
        closureNotes = notes;
        touch(actorId);
    }

    private void requireStatus(CropCycleStatus expected) {
        if (status != expected) throw new IllegalStateException("Expected status " + expected);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID cropPlanId() { return cropPlanId; }
    public UUID fieldId() { return fieldId; }
    public UUID cropId() { return cropId; }
    public UUID varietyId() { return varietyId; }
    public String seasonCode() { return seasonCode; }
    public BigDecimal plannedAreaHectares() { return plannedAreaHectares; }
    public CropCycleStatus status() { return status; }
    public String currentStageCode() { return currentStageCode; }
    public Instant activatedAt() { return activatedAt; }
    public LocalDate sowingDate() { return sowingDate; }
    public Instant closedAt() { return closedAt; }
    public String closureNotes() { return closureNotes; }
    public long version() { return version; }
}
