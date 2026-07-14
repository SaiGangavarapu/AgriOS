package com.agrios.platform.weather.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MonsoonSeasonRepository extends JpaRepository<MonsoonSeasonEntity, UUID> {
    Optional<MonsoonSeasonEntity> findByTenantIdAndRegionCodeAndSeasonYearAndMonsoonType(
            UUID tenantId, String regionCode, int seasonYear, String monsoonType);
}
