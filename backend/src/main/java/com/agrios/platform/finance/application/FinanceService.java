package com.agrios.platform.finance.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.agrios.platform.finance.api.FinanceDtos;
import com.agrios.platform.finance.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceService {
    private final FinancialEventRepository events;
    private final CostCenterRepository costCenters;
    private final BudgetPlanRepository budgets;
    private final BudgetLineRepository budgetLines;
    private final ProfitabilitySnapshotRepository profitability;
    private final IncomeAggregationSnapshotRepository incomeSnapshots;
    private final FarmerRepository farmers;
    private final ObjectMapper mapper;

    public FinanceService(
            FinancialEventRepository events,
            CostCenterRepository costCenters,
            BudgetPlanRepository budgets,
            BudgetLineRepository budgetLines,
            ProfitabilitySnapshotRepository profitability,
            IncomeAggregationSnapshotRepository incomeSnapshots,
            FarmerRepository farmers,
            ObjectMapper mapper) {
        this.events = events;
        this.costCenters = costCenters;
        this.budgets = budgets;
        this.budgetLines = budgetLines;
        this.profitability = profitability;
        this.incomeSnapshots = incomeSnapshots;
        this.farmers = farmers;
        this.mapper = mapper;
    }

    @Transactional
    public FinanceDtos.FinancialEventResponse ingestEvent(
            UUID tenantId, FinanceDtos.FinancialEventRequest request) {
        if (request.farmerId() != null) {
            farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARMER_NOT_FOUND", "Farmer not found."));
        }
        if (events.existsByTenantIdAndIdempotencyKey(
                tenantId, request.idempotencyKey())) {
            throw new ConflictException(
                    "FINANCIAL_EVENT_DUPLICATE",
                    "Financial event idempotency key already exists.");
        }

        FinancialEventEntity event = FinancialEventEntity.create(
                tenantId, request.farmerId(), request.eventType(),
                request.sourceModule(), request.sourceReferenceType(),
                request.sourceReferenceId(), request.eventDate(),
                request.currencyCode(), request.amount(), request.direction(),
                request.description(), request.fieldId(), request.cropCycleId(),
                request.costCenterCode(), request.idempotencyKey(),
                json(request.payload() == null ? Map.of() : request.payload()));
        event.markPosted();
        return FinanceDtos.FinancialEventResponse.from(events.save(event));
    }

    @Transactional
    public UUID createCostCenter(UUID tenantId,
                                 FinanceDtos.CostCenterRequest request) {
        return costCenters.save(CostCenterEntity.create(
                tenantId, request.code(),
                request.name(), request.category())).id();
    }

    @Transactional
    public UUID createBudget(UUID tenantId, FinanceDtos.BudgetRequest request) {
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));

        BudgetPlanEntity plan = budgets.save(BudgetPlanEntity.create(
                tenantId, request.farmerId(), request.cropCycleId(),
                request.fieldId(), request.budgetName(),
                request.periodStart(), request.periodEnd(),
                request.currencyCode()));

        if (request.lines() != null) {
            for (FinanceDtos.BudgetLineRequest line : request.lines()) {
                if (line.costCenterId() != null) {
                    costCenters.findByIdAndTenantId(line.costCenterId(), tenantId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "COST_CENTER_NOT_FOUND", "Cost center not found."));
                }
                budgetLines.save(BudgetLineEntity.create(
                        plan.id(), line.costCenterId(),
                        line.incomeCategory(),
                        line.plannedAmount(), line.notes()));
            }
        }
        return plan.id();
    }

    @Transactional
    public void approveBudget(UUID tenantId, UUID actorId, UUID budgetId) {
        budgets.findByIdAndTenantId(budgetId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BUDGET_NOT_FOUND", "Budget not found."))
                .approve(actorId);
    }

    @Transactional
    public FinanceDtos.ProfitabilityResponse calculateProfitability(
            UUID tenantId, FinanceDtos.ProfitabilityRequest request) {
        farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));

        List<FinancialEventEntity> periodEvents =
                events.findByTenantIdAndFarmerIdAndEventDateBetweenAndStatus(
                        tenantId, request.farmerId(),
                        request.periodStart(), request.periodEnd(), "POSTED");

        BigDecimal revenue = periodEvents.stream()
                .filter(e -> "INFLOW".equals(e.direction()))
                .map(FinancialEventEntity::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal operatingCost = periodEvents.stream()
                .filter(e -> "OUTFLOW".equals(e.direction()))
                .map(FinancialEventEntity::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ProfitabilitySnapshotEntity snapshot =
                ProfitabilitySnapshotEntity.create(
                        tenantId, request.farmerId(),
                        request.fieldId(), request.cropCycleId(),
                        request.periodStart(), request.periodEnd(),
                        revenue, operatingCost,
                        zero(request.allocatedOverhead()),
                        zero(request.depreciation()),
                        zero(request.financeCost()),
                        request.productionQuantity(),
                        request.productionUnit());

        return FinanceDtos.ProfitabilityResponse.from(
                profitability.save(snapshot));
    }

    @Transactional
    public UUID aggregateIncome(UUID tenantId, UUID farmerId,
                                java.time.LocalDate start,
                                java.time.LocalDate end) {
        farmers.findByIdAndTenantId(farmerId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));

        List<FinancialEventEntity> periodEvents =
                events.findByTenantIdAndFarmerIdAndEventDateBetweenAndStatus(
                        tenantId, farmerId, start, end, "POSTED");

        Map<String, BigDecimal> values = new HashMap<>();
        for (FinancialEventEntity event : periodEvents) {
            if (!"INFLOW".equals(event.direction())) continue;
            values.merge(event.sourceModule(), event.amount(), BigDecimal::add);
        }

        IncomeAggregationSnapshotEntity snapshot =
                IncomeAggregationSnapshotEntity.create(
                        tenantId, farmerId, start, end,
                        values.getOrDefault("CROP_SALES", BigDecimal.ZERO),
                        values.getOrDefault("LIVESTOCK", BigDecimal.ZERO),
                        values.getOrDefault("DAIRY", BigDecimal.ZERO),
                        values.getOrDefault("MARKETPLACE", BigDecimal.ZERO),
                        values.getOrDefault("SELLER_ERP", BigDecimal.ZERO),
                        values.getOrDefault("SUBSIDY", BigDecimal.ZERO),
                        values.getOrDefault("INSURANCE", BigDecimal.ZERO),
                        values.getOrDefault("OTHER", BigDecimal.ZERO));

        return incomeSnapshots.save(snapshot).id();
    }

    private BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(
                    "JSON_SERIALIZATION_FAILED",
                    "Unable to serialize finance data.", 500);
        }
    }
}
