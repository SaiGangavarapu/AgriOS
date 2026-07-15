package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inspection", schema = "compliance")
public class InspectionEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID complianceProfileId;
    private UUID certificationApplicationId;
    @Column(nullable = false) private String inspectionType;
    @Column(nullable = false) private Instant scheduledAt;
    private Instant startedAt;
    private Instant completedAt;
    private String inspectorName;
    private String inspectorReference;
    @Column(nullable = false) private String status;
    private String overallResult;
    @Column(columnDefinition = "text") private String notes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected InspectionEntity() {}

    public static InspectionEntity create(
            UUID tenantId, UUID profileId, UUID applicationId,
            String inspectionType, Instant scheduledAt,
            String inspectorName, String inspectorReference, String notes) {
        InspectionEntity value = new InspectionEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.complianceProfileId = profileId;
        value.certificationApplicationId = applicationId;
        value.inspectionType = inspectionType;
        value.scheduledAt = scheduledAt;
        value.inspectorName = inspectorName;
        value.inspectorReference = inspectorReference;
        value.status = "SCHEDULED";
        value.notes = notes;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void start() {
        if (!status.equals("SCHEDULED")) throw new IllegalStateException("Inspection is not scheduled.");
        status = "IN_PROGRESS";
        startedAt = Instant.now();
        updatedAt = startedAt;
    }

    public void complete(String overallResult, String notes) {
        if (!status.equals("IN_PROGRESS")) throw new IllegalStateException("Inspection is not in progress.");
        status = "COMPLETED";
        this.overallResult = overallResult;
        this.notes = notes;
        completedAt = Instant.now();
        updatedAt = completedAt;
    }

    public UUID id() { return id; }
    public String status() { return status; }
    public String overallResult() { return overallResult; }
}
