package com.agrios.platform.consent.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "consent_grant", schema = "consent")
public class ConsentEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmerId;
    @Column(nullable = false) private String purposeCode;
    @Column(nullable = false) private String recipientType;
    @Column(nullable = false) private String recipientId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb") private String dataCategories;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb") private String scopeJson;
    @Column(nullable = false) private String policyVersion;
    @Column(nullable = false) private String language;
    @Column(nullable = false) private Instant validFrom;
    private Instant validUntil;
    @Column(nullable = false) private String status;
    private Instant grantedAt;
    private Instant revokedAt;
    private String revokedReason;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected ConsentEntity() {}

    public static ConsentEntity grant(UUID tenantId, UUID farmerId,
                                      String purpose, String recipientType,
                                      String recipientId, String categoriesJson,
                                      String scopeJson, String policyVersion,
                                      String language, Instant validFrom,
                                      Instant validUntil, UUID actorId) {
        ConsentEntity value = new ConsentEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.farmerId = farmerId;
        value.purposeCode = purpose;
        value.recipientType = recipientType;
        value.recipientId = recipientId;
        value.dataCategories = categoriesJson;
        value.scopeJson = scopeJson;
        value.policyVersion = policyVersion;
        value.language = language;
        value.validFrom = validFrom;
        value.validUntil = validUntil;
        value.status = "ACTIVE";
        value.grantedAt = Instant.now();
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void revoke(String reason, UUID actorId) {
        status = "REVOKED";
        revokedAt = Instant.now();
        revokedReason = reason;
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public boolean activeAt(Instant time) {
        return status.equals("ACTIVE") && !time.isBefore(validFrom)
                && (validUntil == null || time.isBefore(validUntil));
    }

    public UUID id() { return id; }
    public UUID farmerId() { return farmerId; }
    public String purposeCode() { return purposeCode; }
    public String recipientType() { return recipientType; }
    public String recipientId() { return recipientId; }
    public String dataCategories() { return dataCategories; }
    public String scopeJson() { return scopeJson; }
    public String policyVersion() { return policyVersion; }
    public String language() { return language; }
    public Instant validFrom() { return validFrom; }
    public Instant validUntil() { return validUntil; }
    public String status() { return status; }
    public Instant grantedAt() { return grantedAt; }
    public Instant revokedAt() { return revokedAt; }
    public String revokedReason() { return revokedReason; }
    public long version() { return version; }
}
