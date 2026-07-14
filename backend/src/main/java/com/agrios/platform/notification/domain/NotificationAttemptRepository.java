package com.agrios.platform.notification.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationAttemptRepository extends JpaRepository<NotificationAttemptEntity, UUID> {}
