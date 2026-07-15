package com.agrios.platform.ai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ai_provider", schema = "ai")
public class AiProviderEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String providerCode;
    @Column(nullable = false) private String providerName;
    @Column(nullable = false) private String providerType;
    private String endpointReference;
    private String credentialReference;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected AiProviderEntity() {}

    public static AiProviderEntity create(
            UUID tenantId, String code, String name, String type,
            String endpointReference, String credentialReference) {
        AiProviderEntity value = new AiProviderEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.providerCode = code;
        value.providerName = name;
        value.providerType = type;
        value.endpointReference = endpointReference;
        value.credentialReference = credentialReference;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String status() { return status; }
}
