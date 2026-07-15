package com.agrios.platform.traceability.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PackageRepository extends JpaRepository<PackageEntity, UUID> {}
