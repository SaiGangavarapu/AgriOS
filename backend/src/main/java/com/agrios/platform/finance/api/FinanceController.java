package com.agrios.platform.finance.api;

import com.agrios.platform.common.web.TenantContextResolver;
import com.agrios.platform.finance.application.FinanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FinanceController {
    private final FinanceService service;
    private final TenantContextResolver tenants;

    public FinanceController(FinanceService service,
                             TenantContextResolver tenants) {
        this.service = service;
        this.tenants = tenants;
    }

    @PostMapping("/finance/events")
    FinanceDtos.FinancialEventResponse event(
            @Valid @RequestBody FinanceDtos.FinancialEventRequest body,
            HttpServletRequest request) {
        return service.ingestEvent(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/finance/cost-centers")
    Map<String, UUID> costCenter(
            @Valid @RequestBody FinanceDtos.CostCenterRequest body,
            HttpServletRequest request) {
        return Map.of("costCenterId", service.createCostCenter(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/finance/budgets")
    Map<String, UUID> budget(
            @Valid @RequestBody FinanceDtos.BudgetRequest body,
            HttpServletRequest request) {
        return Map.of("budgetId", service.createBudget(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/finance/budgets/{budgetId}/approve")
    void approveBudget(
            @PathVariable UUID budgetId,
            HttpServletRequest request) {
        service.approveBudget(
                tenants.resolve(request).tenantId(),
                actorId(), budgetId);
    }

    @PostMapping("/finance/profitability/calculate")
    FinanceDtos.ProfitabilityResponse profitability(
            @Valid @RequestBody FinanceDtos.ProfitabilityRequest body,
            HttpServletRequest request) {
        return service.calculateProfitability(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/finance/farmers/{farmerId}/income-aggregation")
    Map<String, UUID> aggregateIncome(
            @PathVariable UUID farmerId,
            @RequestParam LocalDate periodStart,
            @RequestParam LocalDate periodEnd,
            HttpServletRequest request) {
        return Map.of("snapshotId", service.aggregateIncome(
                tenants.resolve(request).tenantId(),
                farmerId, periodStart, periodEnd));
    }

    private UUID actorId() {
        return UUID.nameUUIDFromBytes(
                "development-actor".getBytes());
    }
}
