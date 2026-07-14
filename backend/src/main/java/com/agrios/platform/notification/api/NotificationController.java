package com.agrios.platform.notification.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.notification.application.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {
    private final NotificationService service;
    private final TenantContextResolver tenants;

    public NotificationController(NotificationService service,
                                  TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/notifications")
    NotificationDtos.Response create(
            @Valid @RequestBody NotificationDtos.CreateRequest body,
            HttpServletRequest request) {
        return service.create(tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/notifications/{notificationId}/sending")
    NotificationDtos.Response sending(
            @PathVariable UUID notificationId, HttpServletRequest request) {
        return service.markSending(
                tenants.resolve(request).tenantId(), notificationId);
    }

    @PostMapping("/notifications/{notificationId}/delivery-result")
    NotificationDtos.Response deliveryResult(
            @PathVariable UUID notificationId,
            @Valid @RequestBody NotificationDtos.DeliveryResultRequest body,
            HttpServletRequest request) {
        return service.deliveryResult(
                tenants.resolve(request).tenantId(), notificationId, body);
    }

    @PostMapping("/notifications/{notificationId}/read")
    NotificationDtos.Response read(
            @PathVariable UUID notificationId, HttpServletRequest request) {
        return service.markRead(
                tenants.resolve(request).tenantId(), notificationId);
    }

    @GetMapping("/farmers/{farmerId}/notifications")
    List<NotificationDtos.Response> farmerNotifications(
            @PathVariable UUID farmerId, HttpServletRequest request) {
        return service.farmerNotifications(
                tenants.resolve(request).tenantId(), farmerId);
    }
}
