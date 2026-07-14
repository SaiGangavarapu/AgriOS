CREATE TABLE farm.farm (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  programme_id uuid NULL REFERENCES configuration.programme(id),
  name varchar(200) NOT NULL,
  primary_operator_farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  farm_type varchar(60) NOT NULL DEFAULT 'INDIVIDUAL_FAMILY_FARM',
  village_name varchar(160) NOT NULL,
  district_name varchar(160) NULL,
  state_name varchar(160) NULL,
  status varchar(30) NOT NULL DEFAULT 'REGISTERED'
    CHECK (status IN ('DRAFT','REGISTERED','VERIFIED','ACTIVE','INACTIVE','ARCHIVED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE farm.field (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farm_id uuid NOT NULL REFERENCES farm.farm(id),
  name varchar(200) NOT NULL,
  current_boundary_version_no integer NULL,
  area_hectares numeric(12,4) NULL CHECK (area_hectares IS NULL OR area_hectares > 0),
  status varchar(40) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','BOUNDARY_CAPTURED','VALIDATED','VERIFIED','ACTIVE','DISPUTED','SPLIT','MERGED','INACTIVE','ARCHIVED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE farm.field_boundary_version (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  version_no integer NOT NULL,
  boundary geometry(MultiPolygon, 4326) NOT NULL,
  capture_method varchar(40) NOT NULL,
  accuracy_meters numeric(10,2) NULL,
  calculated_area_hectares numeric(12,4) NOT NULL,
  validation_status varchar(30) NOT NULL DEFAULT 'PENDING',
  is_current boolean NOT NULL DEFAULT false,
  captured_at timestamptz NOT NULL DEFAULT now(),
  captured_by uuid NULL,
  verified_at timestamptz NULL,
  verified_by uuid NULL,
  CONSTRAINT uq_field_boundary_version UNIQUE (field_id, version_no)
);

CREATE UNIQUE INDEX uq_field_current_boundary
  ON farm.field_boundary_version(field_id)
  WHERE is_current;

CREATE INDEX ix_field_boundary_gist
  ON farm.field_boundary_version USING gist(boundary);

CREATE TABLE tenure.tenure_arrangement (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  tenure_type varchar(50) NOT NULL,
  cultivator_farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  valid_from date NOT NULL,
  valid_to date NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  evidence_reference varchar(255) NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_tenure_dates CHECK (valid_to IS NULL OR valid_to >= valid_from)
);

CREATE TABLE farm.water_source (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farm_id uuid NOT NULL REFERENCES farm.farm(id),
  source_type varchar(50) NOT NULL,
  name varchar(200) NOT NULL,
  location geometry(Point, 4326) NULL,
  reliability varchar(30) NOT NULL DEFAULT 'UNKNOWN',
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE INDEX ix_water_source_location_gist
  ON farm.water_source USING gist(location);

CREATE TRIGGER trg_farm_updated_at
BEFORE UPDATE ON farm.farm
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_field_updated_at
BEFORE UPDATE ON farm.field
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_tenure_updated_at
BEFORE UPDATE ON tenure.tenure_arrangement
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_water_source_updated_at
BEFORE UPDATE ON farm.water_source
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
