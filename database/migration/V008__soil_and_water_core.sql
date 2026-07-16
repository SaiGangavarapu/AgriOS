CREATE SCHEMA IF NOT EXISTS soilwater;

ALTER TABLE IF EXISTS public.event_publication
  ALTER COLUMN serialized_event TYPE text;

ALTER TABLE IF EXISTS public.event_publication_archive
  ALTER COLUMN serialized_event TYPE text;

CREATE TABLE soilwater.laboratory (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  name varchar(200) NOT NULL,
  laboratory_type varchar(60) NOT NULL DEFAULT 'SOIL_AND_WATER',
  accreditation_reference varchar(200) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_laboratory_tenant_code UNIQUE (tenant_id, code)
);

CREATE TABLE soilwater.sample (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NULL REFERENCES farm.field(id),
  water_source_id uuid NULL REFERENCES farm.water_source(id),
  sample_type varchar(30) NOT NULL
    CHECK (sample_type IN ('SOIL','WATER')),
  sample_code varchar(100) NOT NULL,
  collected_at timestamptz NOT NULL,
  collected_by uuid NULL,
  collection_depth_cm numeric(8,2) NULL,
  collection_method varchar(80) NOT NULL,
  recent_input_notes text NULL,
  status varchar(40) NOT NULL DEFAULT 'COLLECTED'
    CHECK (status IN ('PLANNED','COLLECTED','SEALED','DISPATCHED','RECEIVED','REJECTED','TESTED','PUBLISHED','LOST','CONTAMINATED')),
  rejection_reason text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_sample_tenant_code UNIQUE (tenant_id, sample_code),
  CONSTRAINT ck_sample_subject CHECK (
    (sample_type = 'SOIL' AND field_id IS NOT NULL AND water_source_id IS NULL)
    OR
    (sample_type = 'WATER' AND water_source_id IS NOT NULL AND field_id IS NULL)
  )
);

CREATE TABLE soilwater.test_report (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  sample_id uuid NOT NULL REFERENCES soilwater.sample(id),
  laboratory_id uuid NOT NULL REFERENCES soilwater.laboratory(id),
  report_number varchar(120) NOT NULL,
  tested_at timestamptz NOT NULL,
  published_at timestamptz NULL,
  quality_status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (quality_status IN ('DRAFT','VALID','SUSPECT','REJECTED','SUPERSEDED')),
  interpretation_rule_version varchar(80) NULL,
  valid_until date NULL,
  notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_test_report_lab_number UNIQUE (laboratory_id, report_number)
);

CREATE TABLE soilwater.test_result (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  test_report_id uuid NOT NULL REFERENCES soilwater.test_report(id) ON DELETE CASCADE,
  parameter_code varchar(80) NOT NULL,
  value numeric(18,6) NOT NULL,
  unit_code varchar(40) NOT NULL,
  analytical_method varchar(160) NOT NULL,
  quality_flag varchar(30) NOT NULL DEFAULT 'VALID',
  reference_min numeric(18,6) NULL,
  reference_max numeric(18,6) NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_test_result_parameter UNIQUE (test_report_id, parameter_code)
);

CREATE TABLE soilwater.profile (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  profile_type varchar(30) NOT NULL CHECK (profile_type IN ('SOIL','WATER')),
  field_id uuid NULL REFERENCES farm.field(id),
  water_source_id uuid NULL REFERENCES farm.water_source(id),
  source_report_id uuid NOT NULL REFERENCES soilwater.test_report(id),
  summary_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  constraint_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  effective_from timestamptz NOT NULL,
  valid_until date NULL,
  is_current boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_profile_subject CHECK (
    (profile_type = 'SOIL' AND field_id IS NOT NULL AND water_source_id IS NULL)
    OR
    (profile_type = 'WATER' AND water_source_id IS NOT NULL AND field_id IS NULL)
  )
);

CREATE UNIQUE INDEX uq_current_soil_profile
  ON soilwater.profile(field_id)
  WHERE profile_type = 'SOIL' AND is_current;

CREATE UNIQUE INDEX uq_current_water_profile
  ON soilwater.profile(water_source_id)
  WHERE profile_type = 'WATER' AND is_current;

CREATE INDEX ix_sample_field_time
  ON soilwater.sample(field_id, collected_at DESC)
  WHERE field_id IS NOT NULL;

CREATE INDEX ix_sample_water_time
  ON soilwater.sample(water_source_id, collected_at DESC)
  WHERE water_source_id IS NOT NULL;

CREATE INDEX ix_test_report_sample
  ON soilwater.test_report(sample_id, tested_at DESC);

CREATE TRIGGER trg_laboratory_updated_at
BEFORE UPDATE ON soilwater.laboratory
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_sample_updated_at
BEFORE UPDATE ON soilwater.sample
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_test_report_updated_at
BEFORE UPDATE ON soilwater.test_report
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
