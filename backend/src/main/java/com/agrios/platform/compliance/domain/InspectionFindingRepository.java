package com.agrios.platform.compliance.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InspectionFindingRepository extends JpaRepository<InspectionFindingEntity, UUID> {}
