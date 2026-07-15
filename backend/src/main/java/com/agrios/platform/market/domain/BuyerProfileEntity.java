package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "buyer_profile", schema = "market")
public class BuyerProfileEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String buyerCode;
    @Column(nullable = false) private String buyerName;
    @Column(nullable = false) private String buyerType;
    private String mobile;
    private String email;
    private String gstin;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String addressJson;
    private BigDecimal rating;
    private BigDecimal paymentReliabilityScore;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected BuyerProfileEntity() {}

    public static BuyerProfileEntity create(
            UUID tenantId, String code, String name, String type,
            String mobile, String email, String gstin, String addressJson,
            BigDecimal rating, BigDecimal reliability) {
        BuyerProfileEntity value = new BuyerProfileEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.buyerCode = code;
        value.buyerName = name;
        value.buyerType = type;
        value.mobile = mobile;
        value.email = email;
        value.gstin = gstin;
        value.addressJson = addressJson;
        value.rating = rating;
        value.paymentReliabilityScore = reliability;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public BigDecimal paymentReliabilityScore() { return paymentReliabilityScore; }
}
