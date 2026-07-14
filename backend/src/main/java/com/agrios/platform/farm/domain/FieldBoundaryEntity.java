package com.agrios.platform.farm.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.locationtech.jts.geom.MultiPolygon;

@Entity
@Table(name = "field_boundary_version", schema = "farm")
public class FieldBoundaryEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID fieldId;
    @Column(nullable = false) private int versionNo;
    @Column(nullable = false, columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon boundary;
    @Column(nullable = false) private String captureMethod;
    private BigDecimal accuracyMeters;
    @Column(nullable = false) private BigDecimal calculatedAreaHectares;
    @Column(nullable = false) private String validationStatus;
    @Column(nullable = false) private boolean isCurrent;
    @Column(nullable = false) private Instant capturedAt;
    private UUID capturedBy;
    private Instant verifiedAt;
    private UUID verifiedBy;

    protected FieldBoundaryEntity() {}

    public static FieldBoundaryEntity create(UUID fieldId, int versionNo,
                                             MultiPolygon boundary, String method,
                                             BigDecimal accuracy, BigDecimal area,
                                             UUID actorId) {
        FieldBoundaryEntity value = new FieldBoundaryEntity();
        value.id = UUID.randomUUID();
        value.fieldId = fieldId;
        value.versionNo = versionNo;
        value.boundary = boundary;
        value.captureMethod = method;
        value.accuracyMeters = accuracy;
        value.calculatedAreaHectares = area;
        value.validationStatus = "VALID";
        value.isCurrent = true;
        value.capturedAt = Instant.now();
        value.capturedBy = actorId;
        return value;
    }

    public void retire() { isCurrent = false; }
    public UUID id() { return id; }
    public UUID fieldId() { return fieldId; }
    public int versionNo() { return versionNo; }
    public BigDecimal calculatedAreaHectares() { return calculatedAreaHectares; }
    public String validationStatus() { return validationStatus; }
    public boolean current() { return isCurrent; }
    public Instant capturedAt() { return capturedAt; }
}
