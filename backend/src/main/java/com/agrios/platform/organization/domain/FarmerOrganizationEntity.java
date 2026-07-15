package com.agrios.platform.organization.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "farmer_organization", schema = "organization")
public class FarmerOrganizationEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private String organizationCode;
    @Column(nullable = false) private String organizationName;
    @Column(nullable = false) private String organizationType;
    private String registrationNumber;
    private String registrationAuthority;
    private LocalDate registrationDate;
    private String primaryGeographyCode;
    @Column(nullable = false) private String status;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected FarmerOrganizationEntity() {}

    public static FarmerOrganizationEntity create(
            UUID tenantId, String code, String name, String type,
            String registrationNumber, String registrationAuthority,
            LocalDate registrationDate, String geographyCode) {
        FarmerOrganizationEntity value = new FarmerOrganizationEntity();
        value.id = UUID.randomUUID();
        value.tenantId = tenantId;
        value.organizationCode = code;
        value.organizationName = name;
        value.organizationType = type;
        value.registrationNumber = registrationNumber;
        value.registrationAuthority = registrationAuthority;
        value.registrationDate = registrationDate;
        value.primaryGeographyCode = geographyCode;
        value.status = "ACTIVE";
        value.createdAt = Instant.now();
        value.updatedAt = value.createdAt;
        return value;
    }

    public UUID id() { return id; }
    public String organizationCode() { return organizationCode; }
    public String organizationName() { return organizationName; }
    public String organizationType() { return organizationType; }
    public String status() { return status; }
    public long version() { return version; }
}
