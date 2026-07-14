package com.agrios.platform.seed.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GerminationTestRepository extends JpaRepository<GerminationTestEntity, UUID> {}
