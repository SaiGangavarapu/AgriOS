CREATE TABLE knowledge.crop (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  scientific_name varchar(200) NULL,
  default_name varchar(200) NOT NULL,
  crop_category varchar(80) NOT NULL,
  duration_min_days integer NULL,
  duration_max_days integer NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','IN_REVIEW','APPROVED','PUBLISHED','SUPERSEDED','WITHDRAWN')),
  evidence_grade varchar(30) NOT NULL DEFAULT 'UNASSESSED',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_crop_tenant_code UNIQUE (tenant_id, code),
  CONSTRAINT ck_crop_duration CHECK (
    duration_min_days IS NULL OR duration_max_days IS NULL OR duration_max_days >= duration_min_days
  )
);

CREATE TABLE knowledge.variety (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  code varchar(80) NOT NULL,
  default_name varchar(200) NOT NULL,
  release_status varchar(60) NOT NULL DEFAULT 'REGISTERED',
  duration_days integer NULL,
  season_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  geography_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  tolerance_traits jsonb NOT NULL DEFAULT '[]'::jsonb,
  resistance_traits jsonb NOT NULL DEFAULT '[]'::jsonb,
  market_traits jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','IN_REVIEW','APPROVED','PUBLISHED','SUPERSEDED','WITHDRAWN')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_variety_crop_code UNIQUE (crop_id, code)
);

CREATE TABLE knowledge.crop_requirement (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id) ON DELETE CASCADE,
  requirement_type varchar(60) NOT NULL,
  minimum_value numeric(18,6) NULL,
  maximum_value numeric(18,6) NULL,
  unit_code varchar(40) NULL,
  hard_constraint boolean NOT NULL DEFAULT false,
  applicability_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  evidence_reference varchar(255) NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE knowledge.growth_stage (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id) ON DELETE CASCADE,
  code varchar(80) NOT NULL,
  name varchar(160) NOT NULL,
  sequence_no integer NOT NULL,
  typical_start_day integer NULL,
  typical_end_day integer NULL,
  description text NULL,
  CONSTRAINT uq_growth_stage_crop_code UNIQUE (crop_id, code),
  CONSTRAINT uq_growth_stage_crop_sequence UNIQUE (crop_id, sequence_no)
);

CREATE INDEX ix_crop_tenant_status
  ON knowledge.crop(tenant_id, status);

CREATE INDEX ix_variety_crop_status
  ON knowledge.variety(crop_id, status);

CREATE TRIGGER trg_crop_updated_at
BEFORE UPDATE ON knowledge.crop
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_variety_updated_at
BEFORE UPDATE ON knowledge.variety
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
