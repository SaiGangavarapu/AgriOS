package com.agrios.platform.soilwater.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "test_result", schema = "soilwater")
public class TestResultEntity {
    @Id private UUID id;
    @Column(name = "test_report_id", insertable = false, updatable = false) private UUID testReportId;
    @Column(nullable = false) private String parameterCode;
    @Column(nullable = false, precision = 18, scale = 6) private BigDecimal value;
    @Column(nullable = false) private String unitCode;
    @Column(nullable = false) private String analyticalMethod;
    @Column(nullable = false) private String qualityFlag;
    private BigDecimal referenceMin;
    private BigDecimal referenceMax;
    @Column(nullable = false) private Instant createdAt;

    protected TestResultEntity() {}

    static TestResultEntity create(UUID reportId, String parameter, BigDecimal value,
                                   String unit, String method, String qualityFlag,
                                   BigDecimal min, BigDecimal max) {
        TestResultEntity result = new TestResultEntity();
        result.id = UUID.randomUUID();
        result.testReportId = reportId;
        result.parameterCode = parameter;
        result.value = value;
        result.unitCode = unit;
        result.analyticalMethod = method;
        result.qualityFlag = qualityFlag == null ? "VALID" : qualityFlag;
        result.referenceMin = min;
        result.referenceMax = max;
        result.createdAt = Instant.now();
        return result;
    }

    public UUID id() { return id; }
    public String parameterCode() { return parameterCode; }
    public BigDecimal value() { return value; }
    public String unitCode() { return unitCode; }
    public String analyticalMethod() { return analyticalMethod; }
    public String qualityFlag() { return qualityFlag; }
    public BigDecimal referenceMin() { return referenceMin; }
    public BigDecimal referenceMax() { return referenceMax; }
}
