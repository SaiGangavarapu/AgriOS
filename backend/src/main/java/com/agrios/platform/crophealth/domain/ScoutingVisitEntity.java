package com.agrios.platform.crophealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "scouting_visit", schema = "crophealth")
public class ScoutingVisitEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private UUID cropCycleId;
    @Column(nullable = false) private Instant scoutedAt;
    @Column(nullable = false) private String scoutType;
    private UUID scoutId;
    @Column(nullable = false) private String samplingMethod;
    private BigDecimal sampleAreaHectares;
    private Integer plantCount;
    @Column(nullable = false) private String status;
    @Column(columnDefinition = "text") private String notes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String weatherSnapshot;
    @Column(nullable = false) private Instant createdAt;

    protected ScoutingVisitEntity() {}

    public static ScoutingVisitEntity create(UUID tenantId, UUID fieldId, UUID cycleId,
                                             Instant scoutedAt, String scoutType,
                                             UUID scoutId, String samplingMethod,
                                             BigDecimal sampleArea, Integer plantCount,
                                             String notes, String weatherSnapshot) {
        ScoutingVisitEntity value = new ScoutingVisitEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.cropCycleId = cycleId;
        value.scoutedAt = scoutedAt;
        value.scoutType = scoutType;
        value.scoutId = scoutId;
        value.samplingMethod = samplingMethod;
        value.sampleAreaHectares = sampleArea;
        value.plantCount = plantCount;
        value.status = "COMPLETED";
        value.notes = notes;
        value.weatherSnapshot = weatherSnapshot;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public UUID cropCycleId() { return cropCycleId; }
    public Instant scoutedAt() { return scoutedAt; }
    public String scoutType() { return scoutType; }
    public String samplingMethod() { return samplingMethod; }
}
