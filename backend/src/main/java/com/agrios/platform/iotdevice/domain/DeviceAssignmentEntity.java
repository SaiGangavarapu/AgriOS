package com.agrios.platform.iotdevice.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "device_assignment", schema = "iotdevice")
public class DeviceAssignmentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID deviceId;
    @Column(nullable = false) private String assignmentType;
    @Column(nullable = false) private UUID assignedEntityId;
    @Column(nullable = false) private Instant validFrom;
    private Instant validUntil;
    @Column(nullable = false) private boolean isCurrent;
    @Column(columnDefinition = "text") private String notes;
    private UUID assignedBy;
    @Column(nullable = false) private Instant createdAt;

    protected DeviceAssignmentEntity() {}

    public static DeviceAssignmentEntity assign(UUID tenantId, UUID deviceId,
                                                String assignmentType,
                                                UUID entityId, String notes,
                                                UUID actorId) {
        DeviceAssignmentEntity value = new DeviceAssignmentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.deviceId = deviceId;
        value.assignmentType = assignmentType;
        value.assignedEntityId = entityId;
        value.validFrom = Instant.now();
        value.isCurrent = true;
        value.notes = notes;
        value.assignedBy = actorId;
        value.createdAt = Instant.now();
        return value;
    }

    public void end() {
        isCurrent = false;
        validUntil = Instant.now();
    }

    public UUID id() { return id; }
    public UUID id() { return id; }
    public String assignmentType() { return assignmentType; }
    public UUID assignedEntityId() { return assignedEntityId; }
    public boolean current() { return isCurrent; }
}
