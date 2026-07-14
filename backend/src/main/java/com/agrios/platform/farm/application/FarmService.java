package com.agrios.platform.farm.application;

import com.agrios.platform.common.exception.*;
import com.agrios.platform.farm.api.FarmDtos;
import com.agrios.platform.farm.domain.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import java.math.BigDecimal;
import java.util.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmService {
    private static final BigDecimal SQM_PER_HECTARE = BigDecimal.valueOf(10000);

    private final FarmRepository farms;
    private final FieldRepository fields;
    private final FieldBoundaryRepository boundaries;
    private final WaterSourceRepository waterSources;
    private final FarmerRepository farmers;

    public FarmService(FarmRepository farms, FieldRepository fields,
                       FieldBoundaryRepository boundaries,
                       WaterSourceRepository waterSources,
                       FarmerRepository farmers) {
        this.farms = farms;
        this.fields = fields;
        this.boundaries = boundaries;
        this.waterSources = waterSources;
        this.farmers = farmers;
    }

    @Transactional
    public FarmDtos.FarmResponse registerFarm(UUID tenantId, UUID actorId,
                                              FarmDtos.RegisterFarmRequest request) {
        farmers.findByIdAndTenantId(request.primaryOperatorFarmerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARMER_NOT_FOUND", "Primary operator farmer not found."));
        FarmEntity farm = FarmEntity.register(tenantId, request.programmeId(),
                request.name(), request.primaryOperatorFarmerId(),
                request.farmType(), request.villageName(),
                request.districtName(), request.stateName(), actorId);
        return FarmDtos.FarmResponse.from(farms.save(farm));
    }

    @Transactional(readOnly = true)
    public FarmDtos.FarmResponse getFarm(UUID tenantId, UUID farmId) {
        return FarmDtos.FarmResponse.from(requireFarm(tenantId, farmId));
    }

    @Transactional(readOnly = true)
    public Page<FarmDtos.FarmResponse> listFarms(UUID tenantId, UUID farmerId, Pageable pageable) {
        Page<FarmEntity> page = farmerId == null
                ? farms.findByTenantId(tenantId, pageable)
                : farms.findByTenantIdAndPrimaryOperatorFarmerId(tenantId, farmerId, pageable);
        return page.map(FarmDtos.FarmResponse::from);
    }

    @Transactional
    public FarmDtos.FieldResponse registerField(UUID tenantId, UUID actorId,
                                                UUID farmId,
                                                FarmDtos.RegisterFieldRequest request) {
        requireFarm(tenantId, farmId);
        FieldEntity field = FieldEntity.register(tenantId, farmId, request.name(), actorId);
        return FarmDtos.FieldResponse.from(fields.save(field));
    }

    @Transactional(readOnly = true)
    public FarmDtos.FieldResponse getField(UUID tenantId, UUID fieldId) {
        return FarmDtos.FieldResponse.from(requireField(tenantId, fieldId));
    }

    @Transactional(readOnly = true)
    public Page<FarmDtos.FieldResponse> listFields(UUID tenantId, UUID farmId, Pageable pageable) {
        requireFarm(tenantId, farmId);
        return fields.findByTenantIdAndFarmId(tenantId, farmId, pageable)
                .map(FarmDtos.FieldResponse::from);
    }

    @Transactional
    public FarmDtos.BoundaryResponse addBoundary(UUID tenantId, UUID actorId,
                                                 UUID fieldId,
                                                 FarmDtos.BoundaryRequest request) {
        FieldEntity field = requireField(tenantId, fieldId);
        MultiPolygon geometry = parseGeometry(request.geoJson());
        if (!geometry.isValid() || geometry.isEmpty()) {
            throw new BusinessException("FIELD_GEOMETRY_INVALID",
                    "Boundary geometry is invalid.", 422);
        }

        boundaries.findByFieldIdAndIsCurrentTrue(fieldId)
                .ifPresent(FieldBoundaryEntity::retire);
        int versionNo = boundaries.findByFieldIdOrderByVersionNoDesc(fieldId)
                .stream().findFirst().map(v -> v.versionNo() + 1).orElse(1);

        BigDecimal area = calculateAreaHectares(geometry);
        FieldBoundaryEntity boundary = FieldBoundaryEntity.create(
                fieldId, versionNo, geometry, request.captureMethod(),
                request.accuracyMeters(), area, actorId);
        boundaries.save(boundary);
        field.applyBoundary(versionNo, area, actorId);
        return FarmDtos.BoundaryResponse.from(boundary);
    }

    @Transactional(readOnly = true)
    public List<FarmDtos.BoundaryResponse> boundaryHistory(UUID tenantId, UUID fieldId) {
        requireField(tenantId, fieldId);
        return boundaries.findByFieldIdOrderByVersionNoDesc(fieldId)
                .stream().map(FarmDtos.BoundaryResponse::from).toList();
    }

    @Transactional
    public FarmDtos.WaterSourceResponse addWaterSource(UUID tenantId, UUID actorId,
                                                       UUID farmId,
                                                       FarmDtos.WaterSourceRequest request) {
        requireFarm(tenantId, farmId);
        return FarmDtos.WaterSourceResponse.from(waterSources.save(
                WaterSourceEntity.create(tenantId, farmId, request.sourceType(),
                        request.name(), request.reliability(), actorId)));
    }

    @Transactional(readOnly = true)
    public List<FarmDtos.WaterSourceResponse> listWaterSources(UUID tenantId, UUID farmId) {
        requireFarm(tenantId, farmId);
        return waterSources.findByTenantIdAndFarmId(tenantId, farmId)
                .stream().map(FarmDtos.WaterSourceResponse::from).toList();
    }

    private FarmEntity requireFarm(UUID tenantId, UUID id) {
        return farms.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FARM_NOT_FOUND", "Farm not found."));
    }

    private FieldEntity requireField(UUID tenantId, UUID id) {
        return fields.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FIELD_NOT_FOUND", "Field not found."));
    }

    private MultiPolygon parseGeometry(String geoJson) {
        try {
            Geometry geometry = new GeoJsonReader().read(geoJson);
            geometry.setSRID(4326);
            if (geometry instanceof MultiPolygon multi) return multi;
            if (geometry instanceof Polygon polygon) {
                return polygon.getFactory().createMultiPolygon(new Polygon[]{polygon});
            }
            throw new IllegalArgumentException("Only Polygon or MultiPolygon is supported.");
        } catch (Exception ex) {
            throw new BusinessException("FIELD_GEOMETRY_INVALID",
                    "GeoJSON must contain a valid Polygon or MultiPolygon.", 422);
        }
    }

    private BigDecimal calculateAreaHectares(MultiPolygon geometry) {
        // JTS coordinates are geographic degrees. This is a pilot estimate only;
        // production should use PostGIS ST_Area on a projected/geography type.
        Coordinate centroid = geometry.getCentroid().getCoordinate();
        double latitudeRadians = Math.toRadians(centroid.y);
        double metersPerDegreeLat = 111_320d;
        double metersPerDegreeLon = 111_320d * Math.cos(latitudeRadians);
        Geometry copy = geometry.copy();
        copy.apply((CoordinateFilter) c -> {
            c.x = c.x * metersPerDegreeLon;
            c.y = c.y * metersPerDegreeLat;
        });
        return BigDecimal.valueOf(copy.getArea()).divide(SQM_PER_HECTARE, 4,
                java.math.RoundingMode.HALF_UP);
    }
}
