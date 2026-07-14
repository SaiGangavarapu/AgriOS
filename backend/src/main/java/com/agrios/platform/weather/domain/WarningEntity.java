package com.agrios.platform.weather.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "warning", schema = "weather")
public class WarningEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID sourceId;
    @Column(nullable = false) private UUID locationId;
    private String externalWarningId;
    @Column(nullable = false) private String warningType;
    @Column(nullable = false) private String severity;
    @Column(nullable = false) private String headline;
    @Column(columnDefinition = "text") private String description;
    @Column(nullable = false) private Instant validFrom;
    private Instant validUntil;
    @Column(nullable = false) private Instant issuedAt;
    @Column(nullable = false) private String status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String rawPayload;
    @Column(nullable = false) private Instant createdAt;

    protected WarningEntity() {}

    public static WarningEntity create(UUID tenantId, UUID sourceId, UUID locationId,
                                       String externalId, String type, String severity,
                                       String headline, String description,
                                       Instant validFrom, Instant validUntil,
                                       Instant issuedAt, String rawPayload) {
        WarningEntity value = new WarningEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.sourceId = sourceId;
        value.locationId = locationId;
        value.externalWarningId = externalId;
        value.warningType = type;
        value.severity = severity;
        value.headline = headline;
        value.description = description;
        value.validFrom = validFrom;
        value.validUntil = validUntil;
        value.issuedAt = issuedAt;
        value.status = "ACTIVE";
        value.rawPayload = rawPayload;
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
    public String warningType() { return warningType; }
    public String severity() { return severity; }
    public String headline() { return headline; }
    public Instant validFrom() { return validFrom; }
    public Instant validUntil() { return validUntil; }
}
