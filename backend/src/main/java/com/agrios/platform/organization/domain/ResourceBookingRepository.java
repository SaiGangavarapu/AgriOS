package com.agrios.platform.organization.domain;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ResourceBookingRepository extends JpaRepository<ResourceBookingEntity, UUID> {
    boolean existsByResourceIdAndStatusInAndBookingStartLessThanAndBookingEndGreaterThan(
            UUID resourceId, Collection<String> statuses, Instant bookingEnd, Instant bookingStart);
}
