package com.agrios.platform.seed.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SeedAllocationRepository extends JpaRepository<SeedAllocationEntity, UUID> {
    boolean existsBySeedLotIdAndCropCycleId(UUID seedLotId, UUID cropCycleId);
}
