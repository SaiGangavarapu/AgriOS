package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "budget_plan", schema = "finance")
public class BudgetPlanEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    private UUID cropCycleId;
    private UUID fieldId;
    @Column(nullable = false) private String budgetName;
    @Column(nullable = false) private LocalDate periodStart;
    @Column(nullable = false) private LocalDate periodEnd;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private String currencyCode;
    @Version private long version;
    private Instant approvedAt;
    private UUID approvedBy;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected BudgetPlanEntity() {}

    public static BudgetPlanEntity create(
            UUID tenantId, UUID farmerId, UUID cropCycleId, UUID fieldId,
            String name, LocalDate start, LocalDate end, String currencyCode) {
        BudgetPlanEntity value = new BudgetPlanEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.cropCycleId = cropCycleId;
        value.fieldId = fieldId;
        value.budgetName = name;
        value.periodStart = start;
        value.periodEnd = end;
        value.status = "DRAFT";
        value.currencyCode = currencyCode;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public void approve(UUID actorId) {
        if (!status.equals("DRAFT")) throw new IllegalStateException("Budget is not draft.");
        status = "APPROVED";
        approvedAt = Instant.now();
        approvedBy = actorId;
        updatedAt = approvedAt;
    }

    public UUID id() { return id; }
    public String status() { return status; }
    public long version() { return version; }
}
