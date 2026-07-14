package com.agrios.platform.weather.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WeatherLocationRepository extends JpaRepository<WeatherLocationEntity, UUID> {
    Optional<WeatherLocationEntity> findByTenantIdAndFieldId(UUID tenantId, UUID fieldId);
}
