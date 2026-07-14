package com.agrios.platform.farmer.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.api.FarmerDtos;
import com.agrios.platform.farmer.domain.*;
import com.agrios.platform.integration.OutboxService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmerService {
    private final FarmerRepository farmers;
    private final FarmerVerificationRepository verifications;
    private final OutboxService outbox;

    public FarmerService(FarmerRepository farmers,
                         FarmerVerificationRepository verifications,
                         OutboxService outbox) {
        this.farmers = farmers;
        this.verifications = verifications;
        this.outbox = outbox;
    }

    @Transactional
    public FarmerDtos.Response register(UUID tenantId, UUID actorId,
                                        UUID correlationId,
                                        FarmerDtos.RegisterRequest request) {
        if (request.mobileE164() != null &&
                farmers.existsByTenantIdAndMobileE164AndStatusNot(
                        tenantId, request.mobileE164(), FarmerStatus.MERGED)) {
            throw new ConflictException("FARMER_DUPLICATE_REVIEW_REQUIRED",
                    "A farmer with this mobile number already exists.");
        }

        FarmerEntity farmer = FarmerEntity.register(
                tenantId, request.programmeId(), request.fullName(),
                request.preferredName(), request.preferredLanguage(),
                request.mobileE164(), request.villageName(),
                request.districtName(), request.stateName(),
                request.sourceChannel(), actorId);
        farmers.save(farmer);
        outbox.append(tenantId, "Farmer", farmer.id(), farmer.version(),
                "FarmerRegistered", "{"farmerId":"" + farmer.id() + ""}",
                correlationId, null, Instant.now());
        return FarmerDtos.Response.from(farmer);
    }

    @Transactional(readOnly = true)
    public FarmerDtos.Response get(UUID tenantId, UUID farmerId) {
        return FarmerDtos.Response.from(requireFarmer(tenantId, farmerId));
    }

    @Transactional(readOnly = true)
    public Page<FarmerDtos.Response> search(UUID tenantId, String q, Pageable pageable) {
        Page<FarmerEntity> result = (q == null || q.isBlank())
                ? farmers.findByTenantId(tenantId, pageable)
                : farmers.findByTenantIdAndFullNameContainingIgnoreCase(tenantId, q.trim(), pageable);
        return result.map(FarmerDtos.Response::from);
    }

    @Transactional
    public FarmerDtos.Response update(UUID tenantId, UUID actorId, UUID farmerId,
                                      FarmerDtos.UpdateRequest request) {
        FarmerEntity farmer = requireFarmer(tenantId, farmerId);
        if (farmer.version() != request.version()) {
            throw new ConflictException("RESOURCE_VERSION_CONFLICT",
                    "Farmer was updated by another request.");
        }
        farmer.updateProfile(request.fullName(), request.preferredName(),
                request.preferredLanguage(), request.mobileE164(),
                request.villageName(), request.districtName(),
                request.stateName(), actorId);
        return FarmerDtos.Response.from(farmer);
    }

    @Transactional
    public FarmerDtos.Response verify(UUID tenantId, UUID actorId, UUID farmerId,
                                      FarmerDtos.VerifyRequest request) {
        FarmerEntity farmer = requireFarmer(tenantId, farmerId);
        farmer.verify(request.verificationLevel(), actorId);
        verifications.save(FarmerVerificationEntity.create(
                farmer.id(), request.verificationLevel(),
                request.evidenceType(), request.evidenceReference(),
                actorId, request.expiresAt()));
        return FarmerDtos.Response.from(farmer);
    }

    @Transactional
    public FarmerDtos.Response suspend(UUID tenantId, UUID actorId, UUID farmerId) {
        FarmerEntity farmer = requireFarmer(tenantId, farmerId);
        farmer.suspend(actorId);
        return FarmerDtos.Response.from(farmer);
    }

    @Transactional
    public FarmerDtos.Response reactivate(UUID tenantId, UUID actorId, UUID farmerId) {
        FarmerEntity farmer = requireFarmer(tenantId, farmerId);
        farmer.reactivate(actorId);
        return FarmerDtos.Response.from(farmer);
    }

    @Transactional
    public FarmerDtos.Response merge(UUID tenantId, UUID actorId,
                                     FarmerDtos.MergeRequest request) {
        FarmerEntity duplicate = requireFarmer(tenantId, request.duplicateFarmerId());
        FarmerEntity canonical = requireFarmer(tenantId, request.canonicalFarmerId());
        if (canonical.status() == FarmerStatus.MERGED) {
            throw new ConflictException("FARMER_CANONICAL_INVALID",
                    "Canonical farmer is already merged.");
        }
        duplicate.mergeInto(canonical.id(), actorId);
        return FarmerDtos.Response.from(duplicate);
    }

    private FarmerEntity requireFarmer(UUID tenantId, UUID farmerId) {
        return farmers.findByIdAndTenantId(farmerId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Farmer not found."));
    }
}
