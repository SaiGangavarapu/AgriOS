package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "market_source", schema = "market")
public class MarketSourceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String sourceCode;
    @Column(nullable = false) private String sourceName;
    @Column(nullable = false) private String sourceType;
    private String baseUrl;
    private String licenseReference;
    @Column(nullable = false) private int priority;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected MarketSourceEntity() {}

    public static MarketSourceEntity create(
            UUID tenantId, String code, String name, String type,
            String baseUrl, String licenseReference, int priority) {
        MarketSourceEntity value = new MarketSourceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceCode = code;
        value.sourceName = name;
        value.sourceType = type;
        value.baseUrl = baseUrl;
        value.licenseReference = licenseReference;
        value.priority = priority;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
}
