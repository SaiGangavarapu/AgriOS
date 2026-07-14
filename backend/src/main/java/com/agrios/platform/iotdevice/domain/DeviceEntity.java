package com.agrios.platform.iotdevice.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "device", schema = "iotdevice")
public class DeviceEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String deviceUid;
    @Column(nullable = false) private String deviceType;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    @Column(nullable = false) private String communicationProtocol;
    @Column(nullable = false) private String authenticationMode;
    @Column(nullable = false) private String status;
    private Instant lastSeenAt;
    private Instant commissionedAt;
    private Instant retiredAt;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String metadata;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected DeviceEntity() {}

    public static DeviceEntity register(UUID tenantId, String uid, String type,
                                        String manufacturer, String model,
                                        String firmware, String protocol,
                                        String authenticationMode,
                                        String metadata, UUID actorId) {
        DeviceEntity value = new DeviceEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.deviceUid = uid.trim();
        value.deviceType = type;
        value.manufacturer = manufacturer;
        value.model = model;
        value.firmwareVersion = firmware;
        value.communicationProtocol = protocol;
        value.authenticationMode = authenticationMode;
        value.status = "REGISTERED";
        value.metadata = metadata;
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        value.createdBy = actorId;
        value.updatedBy = actorId;
        return value;
    }

    public void provision(UUID actorId) {
        if (!status.equals("REGISTERED")) throw new IllegalStateException("Device must be registered.");
        status = "PROVISIONED";
        touch(actorId);
    }

    public void activate(UUID actorId) {
        if (!status.equals("PROVISIONED") && !status.equals("INACTIVE")) {
            throw new IllegalStateException("Device cannot be activated.");
        }
        status = "ACTIVE";
        commissionedAt = commissionedAt == null ? Instant.now() : commissionedAt;
        touch(actorId);
    }

    public void heartbeat(Instant seenAt, UUID actorId) {
        lastSeenAt = seenAt;
        if (status.equals("INACTIVE")) status = "ACTIVE";
        touch(actorId);
    }

    public void retire(UUID actorId) {
        status = "RETIRED";
        retiredAt = Instant.now();
        touch(actorId);
    }

    private void touch(UUID actorId) {
        updatedAt = Instant.now();
        updatedBy = actorId;
    }

    public UUID id() { return id; }
    public UUID tenantId() { return tenantId; }
    public String deviceUid() { return deviceUid; }
    public String deviceType() { return deviceType; }
    public String communicationProtocol() { return communicationProtocol; }
    public String status() { return status; }
    public Instant lastSeenAt() { return lastSeenAt; }
    public long version() { return version; }
}
