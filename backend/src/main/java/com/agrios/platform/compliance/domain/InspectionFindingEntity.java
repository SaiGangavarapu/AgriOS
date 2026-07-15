package com.agrios.platform.compliance.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "inspection_finding", schema = "compliance")
public class InspectionFindingEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID inspectionId;
    private UUID requirementId;
    @Column(nullable = false) private String findingCode;
    @Column(nullable = false) private String findingType;
    @Column(nullable = false, columnDefinition = "text") private String description;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String evidenceJson;
    @Column(nullable = false) private boolean correctiveActionRequired;
    private LocalDate dueDate;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected InspectionFindingEntity() {}

    public static InspectionFindingEntity create(
            UUID inspectionId, UUID requirementId, String findingCode,
            String findingType, String description, String evidenceJson,
            boolean correctiveActionRequired, LocalDate dueDate) {
        InspectionFindingEntity value = new InspectionFindingEntity();
        value.id = UUID.randomUUID();
        value.inspectionId = inspectionId;
        value.requirementId = requirementId;
        value.findingCode = findingCode;
        value.findingType = findingType;
        value.description = description;
        value.evidenceJson = evidenceJson;
        value.correctiveActionRequired = correctiveActionRequired;
        value.dueDate = dueDate;
        value.status = "OPEN";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
