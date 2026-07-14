package com.agrios.platform.tenure.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "tenure_arrangement", schema = "tenure")
public class TenureEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private String tenureType;
    @Column(nullable = false) private UUID cultivatorFarmerId;
    @Column(nullable = false) private LocalDate validFrom;
    private LocalDate validTo;
    @Column(nullable = false) private String status;
    private String evidenceReference;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected TenureEntity() {}

    public static TenureEntity create(UUID tenantId, UUID fieldId, String type,
                                      UUID cultivator, LocalDate from, LocalDate to,
                                      String evidence, UUID actorId) {
        TenureEntity value = new TenureEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.fieldId = fieldId;
        value.tenureType = type;
        value.cultivatorFarmerId = cultivator;
        value.validFrom = from;
        value.validTo = to;
        value.status = "ACTIVE";
        value.evidenceReference = evidence;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void end(LocalDate endDate, UUID actorId) {
        if (endDate.isBefore(validFrom)) throw new IllegalArgumentException("End date before start date.");
        validTo = endDate;
        status = "ENDED";
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public String tenureType() { return tenureType; }
    public UUID cultivatorFarmerId() { return cultivatorFarmerId; }
    public LocalDate validFrom() { return validFrom; }
    public LocalDate validTo() { return validTo; }
    public String status() { return status; }
    public String evidenceReference() { return evidenceReference; }
    public long version() { return version; }
}
