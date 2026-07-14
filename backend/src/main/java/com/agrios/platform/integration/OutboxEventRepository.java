package com.agrios.platform.integration;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {}
