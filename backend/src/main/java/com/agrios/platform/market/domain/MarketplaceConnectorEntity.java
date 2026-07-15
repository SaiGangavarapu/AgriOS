package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "marketplace_connector", schema = "market")
public class MarketplaceConnectorEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String connectorCode;
    @Column(nullable = false) private String connectorName;
    @Column(nullable = false) private String connectorType;
    private String endpointReference;
    private String authenticationReference;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected MarketplaceConnectorEntity() {}

    public static MarketplaceConnectorEntity create(
            UUID tenantId, String code, String name, String type,
            String endpoint, String authenticationReference) {
        MarketplaceConnectorEntity value = new MarketplaceConnectorEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.connectorCode = code;
        value.connectorName = name;
        value.connectorType = type;
        value.endpointReference = endpoint;
        value.authenticationReference = authenticationReference;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String status() { return status; }
}
