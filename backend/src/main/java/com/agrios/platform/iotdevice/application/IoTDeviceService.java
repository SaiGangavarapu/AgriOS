package com.agrios.platform.iotdevice.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.iotdevice.api.IoTDeviceDtos;
import com.agrios.platform.iotdevice.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IoTDeviceService {
    private final DeviceRepository devices;
    private final DeviceAssignmentRepository assignments;
    private final DeviceHealthRepository health;
    private final FieldRepository fields;
    private final WaterSourceRepository waterSources;
    private final ObjectMapper mapper;

    public IoTDeviceService(DeviceRepository devices,
                            DeviceAssignmentRepository assignments,
                            DeviceHealthRepository health,
                            FieldRepository fields,
                            WaterSourceRepository waterSources,
                            ObjectMapper mapper) {
        this.devices = devices;
        this.assignments = assignments;
        this.health = health;
        this.fields = fields;
        this.waterSources = waterSources;
        this.mapper = mapper;
    }

    @Transactional
    public IoTDeviceDtos.DeviceResponse register(
            UUID tenantId, UUID actorId, IoTDeviceDtos.RegisterRequest request) {
        devices.findByTenantIdAndDeviceUid(tenantId, request.deviceUid()).ifPresent(existing -> {
            throw new ConflictException("DEVICE_UID_ALREADY_EXISTS",
                    "Device UID already exists.");
        });
        DeviceEntity device = DeviceEntity.register(
                tenantId, request.deviceUid(), request.deviceType(),
                request.manufacturer(), request.model(),
                request.firmwareVersion(), request.communicationProtocol(),
                request.authenticationMode(),
                json(request.metadata() == null ? Map.of() : request.metadata()),
                actorId);
        return IoTDeviceDtos.DeviceResponse.from(devices.save(device));
    }

    @Transactional
    public IoTDeviceDtos.DeviceResponse provision(UUID tenantId, UUID actorId, UUID deviceId) {
        DeviceEntity device = requireDevice(tenantId, deviceId);
        device.provision(actorId);
        return IoTDeviceDtos.DeviceResponse.from(device);
    }

    @Transactional
    public IoTDeviceDtos.DeviceResponse activate(UUID tenantId, UUID actorId, UUID deviceId) {
        DeviceEntity device = requireDevice(tenantId, deviceId);
        device.activate(actorId);
        return IoTDeviceDtos.DeviceResponse.from(device);
    }

    @Transactional
    public UUID assign(UUID tenantId, UUID actorId, UUID deviceId,
                       IoTDeviceDtos.AssignmentRequest request) {
        requireDevice(tenantId, deviceId);
        validateAssignmentTarget(tenantId, request.assignmentType(), request.assignedEntityId());
        assignments.findByDeviceIdAndIsCurrentTrue(deviceId)
                .ifPresent(DeviceAssignmentEntity::end);
        return assignments.save(DeviceAssignmentEntity.assign(
                tenantId, deviceId, request.assignmentType(),
                request.assignedEntityId(), request.notes(), actorId)).id();
    }

    @Transactional
    public IoTDeviceDtos.DeviceResponse heartbeat(UUID tenantId, UUID actorId,
                                                  UUID deviceId,
                                                  IoTDeviceDtos.HeartbeatRequest request) {
        DeviceEntity device = requireDevice(tenantId, deviceId);
        device.heartbeat(request.seenAt(), actorId);
        return IoTDeviceDtos.DeviceResponse.from(device);
    }

    @Transactional
    public void health(UUID tenantId, UUID actorId, UUID deviceId,
                       IoTDeviceDtos.HealthRequest request) {
        DeviceEntity device = requireDevice(tenantId, deviceId);
        health.save(DeviceHealthEntity.create(
                device.id(), request.recordedAt(), request.batteryPercent(),
                request.signalStrengthDbm(), request.internalTemperatureC(),
                request.uptimeSeconds(), request.storageFreeBytes(),
                json(request.errorCodes() == null ? List.of() : request.errorCodes()),
                request.healthStatus()));
        device.heartbeat(request.recordedAt(), actorId);
    }

    @Transactional(readOnly = true)
    public List<IoTDeviceDtos.DeviceResponse> active(UUID tenantId) {
        return devices.findByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, "ACTIVE")
                .stream().map(IoTDeviceDtos.DeviceResponse::from).toList();
    }

    private void validateAssignmentTarget(UUID tenantId, String type, UUID entityId) {
        switch (type) {
            case "FIELD" -> fields.findByIdAndTenantId(entityId, tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FIELD_NOT_FOUND", "Field not found."));
            case "WATER_SOURCE" -> waterSources.findByIdAndTenantId(entityId, tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "WATER_SOURCE_NOT_FOUND", "Water source not found."));
            case "FARM", "WEATHER_LOCATION", "IRRIGATION_PLAN" -> { }
            default -> throw new BusinessException("DEVICE_ASSIGNMENT_TYPE_INVALID",
                    "Unsupported assignment type.", 422);
        }
    }

    private DeviceEntity requireDevice(UUID tenantId, UUID deviceId) {
        return devices.findByIdAndTenantId(deviceId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DEVICE_NOT_FOUND", "Device not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("JSON_SERIALIZATION_FAILED",
                    "Unable to serialize device data.", 500);
        }
    }
}
