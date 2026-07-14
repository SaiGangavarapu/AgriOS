package com.agrios.platform.operations.domain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OperationInputRepository extends JpaRepository<OperationInputEntity, UUID> {}
