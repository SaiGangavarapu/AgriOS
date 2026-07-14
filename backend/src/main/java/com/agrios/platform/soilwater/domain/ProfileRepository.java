package com.agrios.platform.soilwater.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
    Optional<ProfileEntity> findByTenantIdAndFieldIdAndProfileTypeAndIsCurrentTrue(
            UUID tenantId, UUID fieldId, String profileType);
    Optional<ProfileEntity> findByTenantIdAndWaterSourceIdAndProfileTypeAndIsCurrentTrue(
            UUID tenantId, UUID waterSourceId, String profileType);
}
