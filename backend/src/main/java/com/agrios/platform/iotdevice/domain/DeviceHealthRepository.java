package com.agrios.platform.iotdevice.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeviceHealthRepository extends JpaRepository<DeviceHealthEntity, UUID> {
    List<DeviceHealthEntity> findTop20ByDeviceIdOrderByRecordedAtDesc(UUID deviceId);
}
