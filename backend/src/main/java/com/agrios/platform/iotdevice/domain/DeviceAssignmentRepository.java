package com.agrios.platform.iotdevice.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeviceAssignmentRepository extends JpaRepository<DeviceAssignmentEntity, UUID> {
    Optional<DeviceAssignmentEntity> findByDeviceIdAndIsCurrentTrue(UUID deviceId);
    List<DeviceAssignmentEntity> findByAssignmentTypeAndAssignedEntityIdAndIsCurrentTrue(
            String assignmentType, UUID assignedEntityId);
}
