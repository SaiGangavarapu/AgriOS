package com.agrios.platform.operations.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.cropcycle.domain.*;
import com.agrios.platform.operations.api.OperationDtos;
import com.agrios.platform.operations.domain.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationService {
    private final FarmOperationRepository operations;
    private final OperationInputRepository inputs;
    private final CropCycleRepository cycles;

    public OperationService(FarmOperationRepository operations,
                            OperationInputRepository inputs,
                            CropCycleRepository cycles) {
        this.operations = operations;
        this.inputs = inputs;
        this.cycles = cycles;
    }

    @Transactional
    public OperationDtos.Response create(UUID tenantId, UUID actorId,
                                         OperationDtos.CreateRequest request) {
        CropCycleEntity cycle = cycles.findByIdAndTenantId(request.cropCycleId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        if (cycle.status() == CropCycleStatus.CLOSED ||
                cycle.status() == CropCycleStatus.CANCELLED) {
            throw new BusinessException("CROP_CYCLE_READ_ONLY",
                    "Operations cannot be added to a closed crop cycle.", 422);
        }
        if (request.areaHectares() != null &&
                request.areaHectares().compareTo(cycle.plannedAreaHectares()) > 0) {
            throw new BusinessException("OPERATION_AREA_EXCEEDS_CROP_AREA",
                    "Operation area exceeds crop-cycle area.", 422);
        }
        FarmOperationEntity operation = operations.save(FarmOperationEntity.create(
                tenantId, cycle.id(), request.operationType(),
                request.operationDate(), request.areaHectares(),
                request.notes(), request.sourceTaskId(), actorId));
        if (request.inputs() != null) {
            request.inputs().forEach(input -> inputs.save(OperationInputEntity.create(
                    operation.id(), input.inputCategory(), input.productName(),
                    input.quantity(), input.unitCode(), input.batchReference(),
                    input.costAmount(), input.currency())));
        }
        return OperationDtos.Response.from(operation);
    }

    @Transactional
    public OperationDtos.Response start(UUID tenantId, UUID actorId, UUID operationId) {
        FarmOperationEntity operation = requireOperation(tenantId, operationId);
        operation.start(actorId);
        return OperationDtos.Response.from(operation);
    }

    @Transactional
    public OperationDtos.Response complete(UUID tenantId, UUID actorId, UUID operationId) {
        FarmOperationEntity operation = requireOperation(tenantId, operationId);
        operation.complete(actorId);
        return OperationDtos.Response.from(operation);
    }

    @Transactional(readOnly = true)
    public List<OperationDtos.Response> list(UUID tenantId, UUID cycleId) {
        cycles.findByIdAndTenantId(cycleId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CROP_CYCLE_NOT_FOUND", "Crop cycle not found."));
        return operations.findByTenantIdAndCropCycleIdOrderByOperationDateDesc(
                        tenantId, cycleId)
                .stream().map(OperationDtos.Response::from).toList();
    }

    private FarmOperationEntity requireOperation(UUID tenantId, UUID operationId) {
        return operations.findByIdAndTenantId(operationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OPERATION_NOT_FOUND", "Farm operation not found."));
    }
}
