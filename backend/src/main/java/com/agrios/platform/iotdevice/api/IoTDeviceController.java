package com.agrios.platform.iotdevice.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.iotdevice.application.IoTDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IoTDeviceController {
    private final IoTDeviceService service;
    private final TenantContextResolver tenants;

    public IoTDeviceController(IoTDeviceService service, TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/iot/devices")
    IoTDeviceDtos.DeviceResponse register(
            @Valid @RequestBody IoTDeviceDtos.RegisterRequest body,
            HttpServletRequest request) {
        return service.register(tenants.resolve(request).tenantId(), actorId(), body);
    }

    @PostMapping("/iot/devices/{deviceId}/provision")
    IoTDeviceDtos.DeviceResponse provision(@PathVariable UUID deviceId,
                                           HttpServletRequest request) {
        return service.provision(tenants.resolve(request).tenantId(), actorId(), deviceId);
    }

    @PostMapping("/iot/devices/{deviceId}/activate")
    IoTDeviceDtos.DeviceResponse activate(@PathVariable UUID deviceId,
                                          HttpServletRequest request) {
        return service.activate(tenants.resolve(request).tenantId(), actorId(), deviceId);
    }

    @PostMapping("/iot/devices/{deviceId}/assignments")
    Map<String, UUID> assign(@PathVariable UUID deviceId,
                             @Valid @RequestBody IoTDeviceDtos.AssignmentRequest body,
                             HttpServletRequest request) {
        return Map.of("assignmentId", service.assign(
                tenants.resolve(request).tenantId(), actorId(), deviceId, body));
    }

    @PostMapping("/iot/devices/{deviceId}/heartbeat")
    IoTDeviceDtos.DeviceResponse heartbeat(
            @PathVariable UUID deviceId,
            @Valid @RequestBody IoTDeviceDtos.HeartbeatRequest body,
            HttpServletRequest request) {
        return service.heartbeat(
                tenants.resolve(request).tenantId(), actorId(), deviceId, body);
    }

    @PostMapping("/iot/devices/{deviceId}/health")
    void health(@PathVariable UUID deviceId,
                @Valid @RequestBody IoTDeviceDtos.HealthRequest body,
                HttpServletRequest request) {
        service.health(tenants.resolve(request).tenantId(), actorId(), deviceId, body);
    }

    @GetMapping("/iot/devices")
    List<IoTDeviceDtos.DeviceResponse> active(HttpServletRequest request) {
        return service.active(tenants.resolve(request).tenantId());
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes("development-actor".getBytes());
    }
}
