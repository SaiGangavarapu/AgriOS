package com.agrios.platform.operations.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operation_input", schema = "operations")
public class OperationInputEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID operationId;
    @Column(nullable = false) private String inputCategory;
    @Column(nullable = false) private String productName;
    @Column(nullable = false) private BigDecimal quantity;
    @Column(nullable = false) private String unitCode;
    private String batchReference;
    private BigDecimal costAmount;
    @Column(nullable = false) private String currency;
    @Column(nullable = false) private Instant createdAt;

    protected OperationInputEntity() {}

    public static OperationInputEntity create(UUID operationId, String category,
                                              String product, BigDecimal quantity,
                                              String unit, String batch,
                                              BigDecimal cost, String currency) {
        OperationInputEntity value = new OperationInputEntity();
        value.id = UUID.randomUUID();
        value.operationId = operationId;
        value.inputCategory = category;
        value.productName = product;
        value.quantity = quantity;
        value.unitCode = unit;
        value.batchReference = batch;
        value.costAmount = cost;
        value.currency = currency == null ? "INR" : currency;
        value.createdAt = Instant.now();
        return value;
    }
}
