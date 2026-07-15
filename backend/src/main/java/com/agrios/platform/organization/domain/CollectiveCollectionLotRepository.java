package com.agrios.platform.organization.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CollectiveCollectionLotRepository extends JpaRepository<CollectiveCollectionLotEntity, UUID> {
    Optional<CollectiveCollectionLotEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
