package com.agrios.platform.finance.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cost_center", schema = "finance")
public class CostCenterEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private boolean active;
    @Column(nullable = false) private Instant createdAt;

    protected CostCenterEntity() {}

    public static CostCenterEntity create(UUID tenantId, String code,
                                          String name, String category) {
        CostCenterEntity value = new CostCenterEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.code = code;
        value.name = name;
        value.category = category;
        value.active = true;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String code() { return code; }
}
