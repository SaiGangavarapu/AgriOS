package com.agrios.platform.farmer.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerVerificationRepository
        extends JpaRepository<FarmerVerificationEntity, UUID> {}
