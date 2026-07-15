package com.agrios.platform.yieldquality.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quality_parameter", schema = "yieldquality")
public class QualityParameterEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropId;
    @Column(nullable = false) private String parameterCode;
    @Column(nullable = false) private String parameterName;
    @Column(nullable = false) private String valueType;
    private String unitCode;
    private BigDecimal minimumValue;
    private BigDecimal maximumValue;
    @Column(nullable = false) private boolean required;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected QualityParameterEntity() {}

    public static QualityParameterEntity create(
            UUID tenantId, UUID cropId, String code, String name,
            String valueType, String unit, BigDecimal min,
            BigDecimal max, boolean required) {
        QualityParameterEntity value = new QualityParameterEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropId = cropId;
        value.parameterCode = code;
        value.parameterName = name;
        value.valueType = valueType;
        value.unitCode = unit;
        value.minimumValue = min;
        value.maximumValue = max;
        value.required = required;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String valueType() { return valueType; }
    public BigDecimal minimumValue() { return minimumValue; }
    public BigDecimal maximumValue() { return maximumValue; }
}
