package com.agrios.platform.organization.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrganizationMembershipRepository extends JpaRepository<OrganizationMembershipEntity, UUID> {
    Optional<OrganizationMembershipEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<OrganizationMembershipEntity> findByOrganizationIdAndStatus(UUID organizationId, String status);
}
