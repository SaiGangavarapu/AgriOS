package com.agrios.platform.operations.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "farm_operation", schema = "operations")
public class FarmOperationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private String operationType;
    @Column(nullable = false) private LocalDate operationDate;
    private Instant startedAt;
    private Instant completedAt;
    private BigDecimal areaHectares;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String notes;
    private UUID performedBy;
    private UUID sourceTaskId;
    private UUID correctionOfOperationId;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected FarmOperationEntity() {}

    public static FarmOperationEntity create(UUID tenantId, UUID cycleId,
                                             String type, LocalDate date,
                                             BigDecimal area, String notes,
                                             UUID taskId, UUID actorId) {
        FarmOperationEntity value = new FarmOperationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropCycleId = cycleId;
        value.operationType = type;
        value.operationDate = date;
        value.areaHectares = area;
        value.status = "PLANNED";
        value.notes = notes;
        value.sourceTaskId = taskId;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void start(UUID actorId) {
        if (!"PLANNED".equals(status)) throw new IllegalStateException("Operation is not planned.");
        status = "IN_PROGRESS";
        startedAt = Instant.now();
        performedBy = actorId;
        touch(actorId);
    }

    public void complete(UUID actorId) {
        if (!"PLANNED".equals(status) && !"IN_PROGRESS".equals(status)) {
            throw new IllegalStateException("Operation cannot be completed.");
        }
        status = "COMPLETED";
        if (startedAt == null) startedAt = Instant.now();
        completedAt = Instant.now();
        performedBy = actorId;
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID cropCycleId() { return cropCycleId; }
    public String operationType() { return operationType; }
    public LocalDate operationDate() { return operationDate; }
    public Instant startedAt() { return startedAt; }
    public Instant completedAt() { return completedAt; }
    public BigDecimal areaHectares() { return areaHectares; }
    public String status() { return status; }
    public String notes() { return notes; }
    public UUID sourceTaskId() { return sourceTaskId; }
    public long version() { return version; }
}
