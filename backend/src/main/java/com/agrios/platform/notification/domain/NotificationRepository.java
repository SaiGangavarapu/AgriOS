package com.agrios.platform.notification.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    Optional<NotificationEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    boolean existsByTenantIdAndIdempotencyKey(UUID tenantId, String idempotencyKey);
    List<NotificationEntity> findByTenantIdAndFarmerIdOrderByCreatedAtDesc(UUID tenantId, UUID farmerId);
}
