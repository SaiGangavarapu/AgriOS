package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "budget_line", schema = "finance")
public class BudgetLineEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID budgetPlanId;
    private UUID costCenterId;
    private String incomeCategory;
    @Column(nullable = false) private BigDecimal plannedAmount;
    @Column(nullable = false) private BigDecimal actualAmount;
    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false) private Instant createdAt;

    protected BudgetLineEntity() {}

    public static BudgetLineEntity create(
            UUID planId, UUID costCenterId, String incomeCategory,
            BigDecimal plannedAmount, String notes) {
        BudgetLineEntity value = new BudgetLineEntity();
        value.id = UUID.randomUUID();
        value.budgetPlanId = planId;
        value.costCenterId = costCenterId;
        value.incomeCategory = incomeCategory;
        value.plannedAmount = plannedAmount;
        value.actualAmount = BigDecimal.ZERO;
        value.notes = notes;
        value.createdAt = Instant.now();
        return value;
    }
}
