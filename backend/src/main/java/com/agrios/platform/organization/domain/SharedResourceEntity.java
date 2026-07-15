package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shared_resource", schema = "organization")
public class SharedResourceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID organizationId;
    @Column(nullable = false) private String resourceCode;
    @Column(nullable = false) private String resourceName;
    @Column(nullable = false) private String resourceType;
    private BigDecimal capacity;
    private String capacityUnit;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected SharedResourceEntity() {}

    public static SharedResourceEntity create(
            UUID tenantId, UUID organizationId, String code, String name,
            String type, BigDecimal capacity, String capacityUnit,
            BigDecimal hourlyRate, BigDecimal dailyRate) {
        SharedResourceEntity value = new SharedResourceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationId = organizationId;
        value.resourceCode = code;
        value.resourceName = name;
        value.resourceType = type;
        value.capacity = capacity;
        value.capacityUnit = capacityUnit;
        value.hourlyRate = hourlyRate;
        value.dailyRate = dailyRate;
        value.status = "AVAILABLE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public UUID organizationId() { return organizationId; }
    public String status() { return status; }
    public BigDecimal hourlyRate() { return hourlyRate; }
    public BigDecimal dailyRate() { return dailyRate; }
}
