package com.agrios.platform.organization.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CollectiveContributionRepository extends JpaRepository<CollectiveContributionEntity, UUID> {}
