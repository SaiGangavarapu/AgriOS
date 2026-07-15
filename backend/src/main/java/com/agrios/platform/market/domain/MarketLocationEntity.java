package com.agrios.platform.market.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "market_location", schema = "market")
public class MarketLocationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String locationCode;
    @Column(nullable = false) private String locationName;
    @Column(nullable = false) private String locationType;
    private String districtCode;
    private String stateCode;
    @Column(nullable = false) private String countryCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Column(nullable = false) private String status;
    @Column(nullable = false) private Instant createdAt;

    protected MarketLocationEntity() {}

    public static MarketLocationEntity create(
            UUID tenantId, String code, String name, String type,
            String district, String state, String country,
            BigDecimal latitude, BigDecimal longitude) {
        MarketLocationEntity value = new MarketLocationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.locationCode = code;
        value.locationName = name;
        value.locationType = type;
        value.districtCode = district;
        value.stateCode = state;
        value.countryCode = country;
        value.latitude = latitude;
        value.longitude = longitude;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        return value;
    }

    public UUID id() { return id; }
}
