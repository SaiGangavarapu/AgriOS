package com.agrios.platform.iotdevice.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    Optional<DeviceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<DeviceEntity> findByTenantIdAndDeviceUid(UUID tenantId, String deviceUid);
    List<DeviceEntity> findByTenantIdAndStatusOrderByCreatedAtDesc(UUID tenantId, String status);
}
