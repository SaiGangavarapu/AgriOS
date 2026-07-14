package com.agrios.platform.consent.domain;

import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<ConsentEntity, UUID> {
    List<ConsentEntity> findByTenantIdAndFarmerIdOrderByCreatedAtDesc(UUID tenantId, UUID farmerId);
    List<ConsentEntity> findByTenantIdAndFarmerIdAndPurposeCodeAndRecipientTypeAndRecipientIdAndStatus(
            UUID tenantId, UUID farmerId, String purposeCode,
            String recipientType, String recipientId, String status);
}
