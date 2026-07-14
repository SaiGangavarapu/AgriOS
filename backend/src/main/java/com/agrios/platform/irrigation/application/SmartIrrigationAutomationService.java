package com.agrios.platform.irrigation.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.iotdevice.domain.*;
import com.agrios.platform.irrigation.api.AutomationDtos;
import com.agrios.platform.irrigation.domain.*;
import com.agrios.platform.telemetry.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartIrrigationAutomationService {
    private final AutomationPolicyRepository policies;
    private final AutomationDecisionRepository decisions;
    private final ActuatorCommandRepository commands;
    private final IrrigationPlanRepository irrigationPlans;
    private final TelemetryStreamRepository streams;
    private final TelemetryReadingRepository readings;
    private final DeviceRepository devices;
    private final ObjectMapper mapper;

    public SmartIrrigationAutomationService(
            AutomationPolicyRepository policies,
            AutomationDecisionRepository decisions,
            ActuatorCommandRepository commands,
            IrrigationPlanRepository irrigationPlans,
            TelemetryStreamRepository streams,
            TelemetryReadingRepository readings,
            DeviceRepository devices,
            ObjectMapper mapper) {
        this.policies = policies;
        this.decisions = decisions;
        this.commands = commands;
        this.irrigationPlans = irrigationPlans;
        this.streams = streams;
        this.readings = readings;
        this.devices = devices;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createPolicy(UUID tenantId, UUID actorId,
                             AutomationDtos.PolicyRequest request) {
        IrrigationPlanEntity plan = irrigationPlans
                .findByIdAndTenantId(request.irrigationPlanId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "IRRIGATION_PLAN_NOT_FOUND", "Irrigation plan not found."));
        DeviceEntity actuator = devices
                .findByIdAndTenantId(request.actuatorDeviceId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ACTUATOR_DEVICE_NOT_FOUND", "Actuator device not found."));
        if (!actuator.status().equals("ACTIVE")) {
            throw new BusinessException("ACTUATOR_DEVICE_NOT_ACTIVE",
                    "Actuator device must be active.", 422);
        }
        validateStream(tenantId, request.moistureStreamId());
        validateStream(tenantId, request.rainStreamId());
        validateStream(tenantId, request.flowStreamId());

        return policies.save(AutomationPolicyEntity.create(
                tenantId, plan.id(), plan.cropCycleId(),
                request.policyName(), request.controlMode(),
                request.moistureStreamId(), request.rainStreamId(),
                request.flowStreamId(), actuator.id(),
                request.moistureTriggerBelow(),
                request.rainSkipThresholdMm(),
                request.maximumRuntimeMinutes(),
                request.minimumIntervalMinutes(),
                request.allowedStartTime(), request.allowedEndTime(),
                request.emergencyStopEnabled(), actorId)).id();
    }

    @Transactional
    public void approvePolicy(UUID tenantId, UUID actorId, UUID policyId) {
        requirePolicy(tenantId, policyId).approve(actorId);
    }

    @Transactional
    public void activatePolicy(UUID tenantId, UUID actorId, UUID policyId) {
        requirePolicy(tenantId, policyId).activate(actorId);
    }

    @Transactional
    public UUID evaluate(UUID tenantId, UUID policyId,
                         AutomationDtos.EvaluateRequest request) {
        AutomationPolicyEntity policy = requirePolicy(tenantId, policyId);
        if (!policy.status().equals("ACTIVE")) {
            throw new BusinessException("AUTOMATION_POLICY_NOT_ACTIVE",
                    "Automation policy is not active.", 422);
        }

        BigDecimal moisture = latestValue(policy.moistureStreamId());
        BigDecimal rain = latestValue(policy.rainStreamId());
        List<String> reasons = new ArrayList<>();
        String decisionType = "NO_ACTION";

        if (rain != null && policy.rainSkipThresholdMm() != null &&
                rain.compareTo(policy.rainSkipThresholdMm()) >= 0) {
            decisionType = "SKIP";
            reasons.add("RAIN_THRESHOLD_REACHED");
        } else if (moisture != null && policy.moistureTriggerBelow() != null &&
                moisture.compareTo(policy.moistureTriggerBelow()) < 0) {
            decisionType = switch (policy.controlMode()) {
                case "ADVISORY_ONLY", "APPROVAL_REQUIRED" -> "REQUIRES_APPROVAL";
                default -> "START";
            };
            reasons.add("SOIL_MOISTURE_BELOW_THRESHOLD");
        } else if (moisture == null) {
            reasons.add("MOISTURE_READING_MISSING");
        }

        Map<String,Object> snapshot = new LinkedHashMap<>();
        snapshot.put("moisture", moisture);
        snapshot.put("rain", rain);
        snapshot.put("policyId", policy.id());

        String status = "REQUIRES_APPROVAL".equals(decisionType)
                ? "PENDING"
                : ("START".equals(decisionType) ? "APPROVED" : "EXECUTED");

        AutomationDecisionEntity decision = decisions.save(
                AutomationDecisionEntity.create(
                        tenantId, policy.id(), request.irrigationScheduleId(),
                        decisionType, status, json(reasons),
                        json(snapshot), confidence(moisture, rain)));
        return decision.id();
    }

    @Transactional
    public void approveDecision(UUID tenantId, UUID actorId, UUID decisionId) {
        decisions.findByIdAndTenantId(decisionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AUTOMATION_DECISION_NOT_FOUND", "Automation decision not found."))
                .approve(actorId);
    }

    @Transactional
    public AutomationDtos.CommandResponse issueCommand(
            UUID tenantId, UUID decisionId, String idempotencyKey) {
        AutomationDecisionEntity decision = decisions
                .findByIdAndTenantId(decisionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AUTOMATION_DECISION_NOT_FOUND", "Automation decision not found."));
        if (!decision.decisionStatus().equals("APPROVED")) {
            throw new BusinessException("AUTOMATION_DECISION_NOT_APPROVED",
                    "Decision must be approved before command issue.", 422);
        }
        if (commands.existsByTenantIdAndIdempotencyKey(tenantId, idempotencyKey)) {
            throw new ConflictException("ACTUATOR_COMMAND_DUPLICATE",
                    "Command idempotency key already exists.");
        }
        AutomationPolicyEntity resolved = policies
                .findByIdAndTenantId(decision.automationPolicyId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AUTOMATION_POLICY_NOT_FOUND", "Automation policy not found."));

        String commandType = "START".equals(decision.decisionType())
                ? "START_PUMP" : "STOP_PUMP";
        ActuatorCommandEntity command = commands.save(
                ActuatorCommandEntity.create(
                        tenantId, decision.id(), resolved.actuatorDeviceId(),
                        commandType,
                        json(Map.of("maximumRuntimeMinutes",
                                resolved.maximumRuntimeMinutes())),
                        idempotencyKey));
        decision.execute();
        return new AutomationDtos.CommandResponse(command.id(), command.status());
    }

    private void validateStream(UUID tenantId, UUID streamId) {
        if (streamId == null) return;
        streams.findByIdAndTenantId(streamId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TELEMETRY_STREAM_NOT_FOUND", "Telemetry stream not found."));
    }

    private BigDecimal latestValue(UUID streamId) {
        if (streamId == null) return null;
        return readings.findTopByStreamIdOrderByObservedAtDesc(streamId)
                .map(TelemetryReadingEntity::numericValue)
                .orElse(null);
    }

    private BigDecimal confidence(BigDecimal moisture, BigDecimal rain) {
        if (moisture != null && rain != null) return new BigDecimal("0.9000");
        if (moisture != null || rain != null) return new BigDecimal("0.6500");
        return new BigDecimal("0.2500");
    }

    private AutomationPolicyEntity requirePolicy(UUID tenantId, UUID policyId) {
        return policies.findByIdAndTenantId(policyId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AUTOMATION_POLICY_NOT_FOUND", "Automation policy not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize automation data.", 500);
        }
    }
}
