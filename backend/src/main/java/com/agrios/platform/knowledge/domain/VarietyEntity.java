package com.agrios.platform.knowledge.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "variety", schema = "knowledge")
public class VarietyEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID cropId;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String defaultName;
    @Column(nullable = false) private String releaseStatus;
    private Integer durationDays;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String seasonCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String geographyCodes;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String toleranceTraits;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String resistanceTraits;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb")
    private String marketTraits;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private KnowledgeStatus status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected VarietyEntity() {}

    public static VarietyEntity create(UUID tenantId, UUID cropId, String code,
                                       String name, String releaseStatus,
                                       Integer durationDays, String seasons,
                                       String geographies, String tolerances,
                                       String resistances, String marketTraits,
                                       UUID actorId) {
        VarietyEntity value = new VarietyEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.cropId = cropId;
        value.code = code.trim().toUpperCase(Locale.ROOT);
        value.defaultName = name.trim();
        value.releaseStatus = releaseStatus;
        value.durationDays = durationDays;
        value.seasonCodes = seasons;
        value.geographyCodes = geographies;
        value.toleranceTraits = tolerances;
        value.resistanceTraits = resistances;
        value.marketTraits = marketTraits;
        value.status = KnowledgeStatus.DRAFT;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public UUID id() { return id; }
    public UUID cropId() { return cropId; }
    public String code() { return code; }
    public String defaultName() { return defaultName; }
    public String releaseStatus() { return releaseStatus; }
    public Integer durationDays() { return durationDays; }
    public String seasonCodes() { return seasonCodes; }
    public String geographyCodes() { return geographyCodes; }
    public String toleranceTraits() { return toleranceTraits; }
    public String resistanceTraits() { return resistanceTraits; }
    public String marketTraits() { return marketTraits; }
    public KnowledgeStatus status() { return status; }
    public long version() { return version; }
}
