package com.agrios.platform.weather.domain;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ObservationRepository extends JpaRepository<ObservationEntity, UUID> {
    List<ObservationEntity> findByLocationIdAndObservedAtBetweenOrderByObservedAtAsc(
            UUID locationId, Instant from, Instant to);
}
