package com.agrios.platform.weather.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ForecastRunRepository extends JpaRepository<ForecastRunEntity, UUID> {
    List<ForecastRunEntity> findByLocationIdAndStatusOrderByIssuedAtDesc(UUID locationId, String status);
}
