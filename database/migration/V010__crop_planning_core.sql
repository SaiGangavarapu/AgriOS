CREATE TABLE cropplanning.suitability_assessment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  requested_by uuid NULL,
  season_code varchar(60) NOT NULL,
  farming_system varchar(60) NOT NULL,
  soil_profile_id uuid NULL REFERENCES soilwater.profile(id),
  water_profile_id uuid NULL REFERENCES soilwater.profile(id),
  assessment_status varchar(30) NOT NULL DEFAULT 'COMPLETED',
  assessed_at timestamptz NOT NULL DEFAULT now(),
  assumptions_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE cropplanning.suitability_candidate (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  assessment_id uuid NOT NULL REFERENCES cropplanning.suitability_assessment(id) ON DELETE CASCADE,
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  suitability_score numeric(8,4) NOT NULL,
  confidence_score numeric(8,4) NOT NULL,
  hard_constraint_failed boolean NOT NULL DEFAULT false,
  hard_constraint_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  rank_no integer NOT NULL,
  estimated_duration_days integer NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_suitability_candidate UNIQUE (assessment_id, crop_id, variety_id)
);

CREATE TABLE cropplanning.crop_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  assessment_id uuid NULL REFERENCES cropplanning.suitability_assessment(id),
  season_code varchar(60) NOT NULL,
  farming_system varchar(60) NOT NULL,
  selected_crop_id uuid NULL REFERENCES knowledge.crop(id),
  selected_variety_id uuid NULL REFERENCES knowledge.variety(id),
  planned_area_hectares numeric(12,4) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','GENERATED','FARMER_REVIEWED','EXPERT_REVIEW','APPROVED','ACTIVATED','REJECTED','CANCELLED','SUPERSEDED')),
  approval_notes text NULL,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_crop_plan_area CHECK (planned_area_hectares > 0)
);

CREATE TABLE cropplanning.crop_plan_scenario (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_plan_id uuid NOT NULL REFERENCES cropplanning.crop_plan(id) ON DELETE CASCADE,
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  scenario_name varchar(160) NOT NULL,
  expected_yield_min numeric(14,4) NULL,
  expected_yield_max numeric(14,4) NULL,
  yield_unit varchar(40) NULL,
  estimated_cost numeric(14,2) NULL,
  currency varchar(3) NOT NULL DEFAULT 'INR',
  suitability_score numeric(8,4) NULL,
  risk_json jsonb NOT NULL DEFAULT '[]'::jsonb,
  resource_requirements_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  selected boolean NOT NULL DEFAULT false,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_suitability_field_season
  ON cropplanning.suitability_assessment(field_id, season_code, assessed_at DESC);

CREATE INDEX ix_crop_plan_field_status
  ON cropplanning.crop_plan(field_id, status, created_at DESC);

CREATE UNIQUE INDEX uq_crop_plan_selected_scenario
  ON cropplanning.crop_plan_scenario(crop_plan_id)
  WHERE selected;

CREATE TRIGGER trg_crop_plan_updated_at
BEFORE UPDATE ON cropplanning.crop_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
