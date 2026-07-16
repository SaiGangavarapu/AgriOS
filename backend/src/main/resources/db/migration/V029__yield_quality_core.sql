CREATE SCHEMA IF NOT EXISTS yieldquality;

CREATE TABLE yieldquality.yield_record (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  harvest_batch_id uuid NOT NULL REFERENCES harvest.harvest_batch(id),
  harvested_quantity numeric(18,4) NOT NULL,
  harvested_unit varchar(40) NOT NULL,
  field_area_hectares numeric(12,4) NOT NULL CHECK (field_area_hectares > 0),
  yield_per_hectare numeric(18,4) NOT NULL,
  expected_yield_per_hectare numeric(18,4) NULL,
  deviation_percent numeric(12,4) NULL,
  marketable_quantity numeric(18,4) NULL,
  rejected_quantity numeric(18,4) NULL,
  recorded_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT uq_yield_record_batch UNIQUE (harvest_batch_id)
);

CREATE TABLE yieldquality.quality_parameter (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  parameter_code varchar(100) NOT NULL,
  parameter_name varchar(200) NOT NULL,
  value_type varchar(30) NOT NULL
    CHECK (value_type IN ('NUMBER','TEXT','BOOLEAN')),
  unit_code varchar(40) NULL,
  minimum_value numeric(18,6) NULL,
  maximum_value numeric(18,6) NULL,
  required boolean NOT NULL DEFAULT true,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','SUPERSEDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_quality_parameter UNIQUE (tenant_id, crop_id, parameter_code)
);

CREATE TABLE yieldquality.quality_assessment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  harvest_batch_id uuid NOT NULL REFERENCES harvest.harvest_batch(id),
  assessed_at timestamptz NOT NULL,
  assessor_type varchar(40) NOT NULL,
  assessor_id uuid NULL,
  status varchar(30) NOT NULL DEFAULT 'COMPLETED'
    CHECK (status IN ('DRAFT','IN_PROGRESS','COMPLETED','REJECTED')),
  assigned_grade varchar(40) NULL,
  marketability_status varchar(30) NOT NULL DEFAULT 'PENDING'
    CHECK (marketability_status IN ('PENDING','MARKETABLE','PROCESSING_ONLY','REJECTED')),
  notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE yieldquality.quality_result (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  quality_assessment_id uuid NOT NULL REFERENCES yieldquality.quality_assessment(id) ON DELETE CASCADE,
  quality_parameter_id uuid NOT NULL REFERENCES yieldquality.quality_parameter(id),
  numeric_value numeric(18,6) NULL,
  text_value varchar(500) NULL,
  boolean_value boolean NULL,
  within_specification boolean NULL,
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_quality_result UNIQUE (quality_assessment_id, quality_parameter_id),
  CONSTRAINT ck_quality_result_value CHECK (
    numeric_value IS NOT NULL OR text_value IS NOT NULL OR boolean_value IS NOT NULL
  )
);

CREATE TABLE yieldquality.grade_rule (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  grade_code varchar(40) NOT NULL,
  grade_rank integer NOT NULL,
  rule_definition jsonb NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_grade_rule UNIQUE (tenant_id, crop_id, grade_code)
);

CREATE INDEX ix_yield_record_cycle
  ON yieldquality.yield_record(crop_cycle_id, recorded_at DESC);

CREATE INDEX ix_quality_assessment_batch
  ON yieldquality.quality_assessment(harvest_batch_id, assessed_at DESC);

CREATE TRIGGER trg_quality_assessment_updated_at
BEFORE UPDATE ON yieldquality.quality_assessment
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
