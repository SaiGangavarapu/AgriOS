package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "monsoon_season", schema = "weather")
public class MonsoonSeasonEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String regionCode;
    @Column(nullable = false) private String monsoonType;
    @Column(nullable = false) private int seasonYear;
    private LocalDate climatologyOnsetDate;
    private LocalDate climatologyWithdrawalDate;
    private LocalDate predictedOnsetDate;
    private LocalDate predictedWithdrawalDate;
    private LocalDate actualOnsetDate;
    private LocalDate actualWithdrawalDate;
    private BigDecimal onsetConfidence;
    private BigDecimal withdrawalConfidence;
    @Column(nullable = false) private String status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String basisJson;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected MonsoonSeasonEntity() {}

    public static MonsoonSeasonEntity create(UUID tenantId, String region,
                                             String type, int year,
                                             LocalDate climatologyOnset,
                                             LocalDate climatologyWithdrawal,
                                             LocalDate predictedOnset,
                                             LocalDate predictedWithdrawal,
                                             BigDecimal onsetConfidence,
                                             BigDecimal withdrawalConfidence,
                                             String basis) {
        MonsoonSeasonEntity value = new MonsoonSeasonEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.regionCode = region;
        value.monsoonType = type;
        value.seasonYear = year;
        value.climatologyOnsetDate = climatologyOnset;
        value.climatologyWithdrawalDate = climatologyWithdrawal;
        value.predictedOnsetDate = predictedOnset;
        value.predictedWithdrawalDate = predictedWithdrawal;
        value.onsetConfidence = onsetConfidence;
        value.withdrawalConfidence = withdrawalConfidence;
        value.status = predictedOnset == null ? "MONITORING" : "ONSET_PREDICTED";
        value.basisJson = basis;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String regionCode() { return regionCode; }
    public String monsoonType() { return monsoonType; }
    public int seasonYear() { return seasonYear; }
    public LocalDate predictedOnsetDate() { return predictedOnsetDate; }
    public LocalDate predictedWithdrawalDate() { return predictedWithdrawalDate; }
    public BigDecimal onsetConfidence() { return onsetConfidence; }
    public String status() { return status; }
    public String basisJson() { return basisJson; }
}
