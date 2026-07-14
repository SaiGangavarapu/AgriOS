package com.agrios.platform.soilwater.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "test_report", schema = "soilwater")
public class TestReportEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID sampleId;
    @Column(nullable = false) private UUID laboratoryId;
    @Column(nullable = false, length = 120) private String reportNumber;
    @Column(nullable = false) private Instant testedAt;
    private Instant publishedAt;
    @Column(nullable = false) private String qualityStatus;
    private String interpretationRuleVersion;
    private LocalDate validUntil;
    @Column(columnDefinition = "text") private String notes;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_report_id", nullable = false)
    private List<TestResultEntity> results = new ArrayList<>();

    protected TestReportEntity() {}

    public static TestReportEntity create(UUID tenantId, UUID sampleId, UUID laboratoryId,
                                          String reportNumber, Instant testedAt,
                                          String ruleVersion, LocalDate validUntil,
                                          String notes, UUID actorId) {
        TestReportEntity value = new TestReportEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sampleId = sampleId;
        value.laboratoryId = laboratoryId;
        value.reportNumber = reportNumber.trim();
        value.testedAt = testedAt;
        value.qualityStatus = "DRAFT";
        value.interpretationRuleVersion = ruleVersion;
        value.validUntil = validUntil;
        value.notes = notes;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void addResult(String parameter, java.math.BigDecimal resultValue,
                          String unit, String method, String qualityFlag,
                          java.math.BigDecimal min, java.math.BigDecimal max) {
        results.add(TestResultEntity.create(id, parameter, resultValue,
                unit, method, qualityFlag, min, max));
    }

    public void publish(UUID actorId) {
        if (results.isEmpty()) throw new IllegalStateException("At least one result is required.");
        qualityStatus = "VALID";
        publishedAt = Instant.now();
        updatedAt = publishedAt;
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public UUID sampleId() { return sampleId; }
    public UUID laboratoryId() { return laboratoryId; }
    public String reportNumber() { return reportNumber; }
    public Instant testedAt() { return testedAt; }
    public Instant publishedAt() { return publishedAt; }
    public String qualityStatus() { return qualityStatus; }
    public String interpretationRuleVersion() { return interpretationRuleVersion; }
    public LocalDate validUntil() { return validUntil; }
    public String notes() { return notes; }
    public long version() { return version; }
    public List<TestResultEntity> results() { return List.copyOf(results); }
}
