package com.agrios.platform.weather.domain;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WarningRepository extends JpaRepository<WarningEntity, UUID> {
    List<WarningEntity> findByLocationIdAndStatusAndValidFromLessThanEqualOrderByIssuedAtDesc(
            UUID locationId, String status, Instant now);
}
