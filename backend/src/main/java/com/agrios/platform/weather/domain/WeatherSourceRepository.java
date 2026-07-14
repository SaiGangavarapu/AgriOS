package com.agrios.platform.weather.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WeatherSourceRepository extends JpaRepository<WeatherSourceEntity, UUID> {
    Optional<WeatherSourceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<WeatherSourceEntity> findByTenantIdAndStatusOrderByPriorityAsc(UUID tenantId, String status);
}
