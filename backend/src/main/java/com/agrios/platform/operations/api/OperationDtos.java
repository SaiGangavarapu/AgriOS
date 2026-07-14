package com.agrios.platform.operations.api;

import com.agrios.platform.operations.domain.FarmOperationEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class OperationDtos {
    private OperationDtos() {}

    public record InputRequest(
            @NotBlank String inputCategory,
            @NotBlank String productName,
            @NotNull @DecimalMin("0.0001") BigDecimal quantity,
            @NotBlank String unitCode,
            String batchReference,
            BigDecimal costAmount,
            String currency) {}

    public record CreateRequest(
            @NotNull UUID cropCycleId,
            @NotBlank String operationType,
            @NotNull LocalDate operationDate,
            @DecimalMin("0.0001") BigDecimal areaHectares,
            String notes,
            UUID sourceTaskId,
            List<@Valid InputRequest> inputs) {}

    public record Response(
            UUID id, UUID cropCycleId, String operationType,
            LocalDate operationDate, Instant startedAt, Instant completedAt,
            BigDecimal areaHectares, String status,
            String notes, UUID sourceTaskId, long version) {
        public static Response from(FarmOperationEntity value) {
            return new Response(value.id(), value.cropCycleId(),
                    value.operationType(), value.operationDate(),
                    value.startedAt(), value.completedAt(),
                    value.areaHectares(), value.status(),
                    value.notes(), value.sourceTaskId(), value.version());
        }
    }
}
