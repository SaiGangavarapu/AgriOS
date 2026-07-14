package com.agrios.platform.tenure.domain;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenureRepository extends JpaRepository<TenureEntity, UUID> {
    List<TenureEntity> findByTenantIdAndFieldIdOrderByValidFromDesc(UUID tenantId, UUID fieldId);
    boolean existsByTenantIdAndFieldIdAndStatusAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            UUID tenantId, UUID fieldId, String status, LocalDate to, LocalDate from);
}
