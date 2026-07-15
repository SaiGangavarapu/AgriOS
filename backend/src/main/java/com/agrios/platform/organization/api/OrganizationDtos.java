package com.agrios.platform.organization.api;

import com.agrios.platform.organization.domain.FarmerOrganizationEntity;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public final class OrganizationDtos {
    private OrganizationDtos() {}

    public record OrganizationRequest(
            @NotBlank String organizationCode,
            @NotBlank String organizationName,
            @NotBlank String organizationType,
            String registrationNumber,
            String registrationAuthority,
            LocalDate registrationDate,
            String primaryGeographyCode) {}

    public record MembershipRequest(
            @NotNull UUID farmerId,
            @NotBlank String membershipNumber,
            @NotBlank String membershipType,
            @NotNull LocalDate joinedOn,
            @DecimalMin("0.0") BigDecimal shareUnits,
            @DecimalMin("0.0") BigDecimal shareValue) {}

    public record SharedResourceRequest(
            @NotBlank String resourceCode,
            @NotBlank String resourceName,
            @NotBlank String resourceType,
            BigDecimal capacity,
            String capacityUnit,
            BigDecimal hourlyRate,
            BigDecimal dailyRate) {}

    public record BookingRequest(
            @NotNull UUID membershipId,
            @NotNull Instant bookingStart,
            @NotNull Instant bookingEnd,
            @NotBlank String purpose) {}

    public record ProcurementRequest(
            @NotBlank String requestCode,
            @NotBlank String itemCategory,
            @NotBlank String itemName,
            @NotNull @DecimalMin("0.0001") BigDecimal targetQuantity,
            @NotBlank String quantityUnit,
            LocalDate requiredBy) {}

    public record CollectionLotRequest(
            @NotBlank String collectionCode,
            @NotNull UUID cropId,
            UUID varietyId,
            String gradeCode,
            @NotBlank String quantityUnit) {}

    public record ContributionRequest(
            @NotNull UUID membershipId,
            UUID traceLotId,
            @NotNull @DecimalMin("0.0001") BigDecimal contributedQuantity,
            String qualityGrade) {}

    public record OrganizationResponse(
            UUID id, String organizationCode,
            String organizationName, String organizationType,
            String status, long version) {
        public static OrganizationResponse from(FarmerOrganizationEntity value) {
            return new OrganizationResponse(
                    value.id(), value.organizationCode(),
                    value.organizationName(), value.organizationType(),
                    value.status(), value.version());
        }
    }
}
