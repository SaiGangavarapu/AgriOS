package com.agrios.platform.irrigation.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "automation_policy", schema = "irrigation")
public class AutomationPolicyEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID irrigationPlanId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private String policyName;
    @Column(nullable = false) private String controlMode;
    private UUID moistureStreamId;
    private UUID rainStreamId;
    private UUID flowStreamId;
    @Column(nullable = false) private UUID actuatorDeviceId;
    private BigDecimal moistureTriggerBelow;
    private BigDecimal rainSkipThresholdMm;
    @Column(nullable = false) private int maximumRuntimeMinutes;
    @Column(nullable = false) private int minimumIntervalMinutes;
    private LocalTime allowedStartTime;
    private LocalTime allowedEndTime;
    @Column(nullable = false) private boolean emergencyStopEnabled;
    @Column(nullable = false) private String status;
    private Instant approvedAt;
    private UUID approvedBy;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected AutomationPolicyEntity() {}

    public static AutomationPolicyEntity create(
            UUID tenantId, UUID planId, UUID cycleId, String name,
            String controlMode, UUID moistureStreamId,
            UUID rainStreamId, UUID flowStreamId,
            UUID actuatorDeviceId, BigDecimal moistureThreshold,
            BigDecimal rainThreshold, int maxRuntime,
            int minInterval, LocalTime allowedStart,
            LocalTime allowedEnd, boolean emergencyStop,
            UUID actorId) {
        AutomationPolicyEntity value = new AutomationPolicyEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.irrigationPlanId = planId;
        value.cropCycleId = cycleId;
        value.policyName = name;
        value.controlMode = controlMode;
        value.moistureStreamId = moistureStreamId;
        value.rainStreamId = rainStreamId;
        value.flowStreamId = flowStreamId;
        value.actuatorDeviceId = actuatorDeviceId;
        value.moistureTriggerBelow = moistureThreshold;
        value.rainSkipThresholdMm = rainThreshold;
        value.maximumRuntimeMinutes = maxRuntime;
        value.minimumIntervalMinutes = minInterval;
        value.allowedStartTime = allowedStart;
        value.allowedEndTime = allowedEnd;
        value.emergencyStopEnabled = emergencyStop;
        value.status = "DRAFT";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("DRAFT")) throw new IllegalStateException("Policy must be draft.");
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
        updatedBy = actorId;
    }

    public void activate(UUID actorId) {
        if (!status.equals("APPROVED") && !status.equals("PAUSED")) {
            throw new IllegalStateException("Policy cannot be activated.");
        }
        status = "ACTIVE";
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID irrigationPlanId() { return irrigationPlanId; }
    public UUID cropCycleId() { return cropCycleId; }
    public String controlMode() { return controlMode; }
    public UUID moistureStreamId() { return moistureStreamId; }
    public UUID rainStreamId() { return rainStreamId; }
    public UUID actuatorDeviceId() { return actuatorDeviceId; }
    public BigDecimal moistureTriggerBelow() { return moistureTriggerBelow; }
    public BigDecimal rainSkipThresholdMm() { return rainSkipThresholdMm; }
    public int maximumRuntimeMinutes() { return maximumRuntimeMinutes; }
    public String status() { return status; }
}
