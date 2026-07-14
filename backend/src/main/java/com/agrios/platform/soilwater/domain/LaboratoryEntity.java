package com.agrios.platform.soilwater.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "laboratory", schema = "soilwater")
public class LaboratoryEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false, length = 80) private String code;
    @Column(nullable = false, length = 200) private String name;
    @Column(nullable = false, length = 60) private String laboratoryType;
    private String accreditationReference;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected LaboratoryEntity() {}

    public static LaboratoryEntity create(UUID tenantId, String code, String name,
                                          String type, String accreditation, UUID actorId) {
        LaboratoryEntity value = new LaboratoryEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.code = code.trim();
        value.name = name.trim();
        value.laboratoryType = type;
        value.accreditationReference = accreditation;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public String code() { return code; }
    public String name() { return name; }
    public String laboratoryType() { return laboratoryType; }
    public String accreditationReference() { return accreditationReference; }
    public String status() { return status; }
    public long version() { return version; }
}
