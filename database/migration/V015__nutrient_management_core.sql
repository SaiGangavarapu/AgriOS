CREATE TABLE nutrient.nutrient_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  soil_profile_id uuid NULL REFERENCES soilwater.profile(id),
  farming_approach varchar(40) NOT NULL
    CHECK (farming_approach IN ('FERTILIZER_FREE','INTEGRATED','FERTILIZER_BASED')),
  target_yield numeric(18,4) NULL,
  target_yield_unit varchar(40) NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','GENERATED','FARMER_REVIEWED','EXPERT_REVIEW','APPROVED','ACTIVE','COMPLETED','CANCELLED','SUPERSEDED')),
  recommendation_basis jsonb NOT NULL DEFAULT '{}'::jsonb,
  warnings jsonb NOT NULL DEFAULT '[]'::jsonb,
  approval_notes text NULL,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE nutrient.nutrient_plan_item (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nutrient_plan_id uuid NOT NULL REFERENCES nutrient.nutrient_plan(id) ON DELETE CASCADE,
  nutrient_code varchar(40) NOT NULL,
  source_category varchar(60) NOT NULL,
  source_name varchar(200) NOT NULL,
  planned_quantity numeric(18,4) NOT NULL CHECK (planned_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  nutrient_content_percent numeric(8,4) NULL,
  planned_nutrient_quantity numeric(18,4) NULL,
  planned_application_date date NULL,
  crop_stage_code varchar(80) NULL,
  application_method varchar(80) NOT NULL,
  split_no integer NULL,
  safety_interval_days integer NULL,
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE nutrient.nutrient_application (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nutrient_plan_id uuid NOT NULL REFERENCES nutrient.nutrient_plan(id),
  nutrient_plan_item_id uuid NULL REFERENCES nutrient.nutrient_plan_item(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  applied_at timestamptz NOT NULL,
  source_category varchar(60) NOT NULL,
  source_name varchar(200) NOT NULL,
  applied_quantity numeric(18,4) NOT NULL CHECK (applied_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  application_method varchar(80) NOT NULL,
  area_hectares numeric(12,4) NULL CHECK (area_hectares IS NULL OR area_hectares > 0),
  weather_check_status varchar(30) NOT NULL DEFAULT 'NOT_CHECKED',
  safety_validation_status varchar(30) NOT NULL DEFAULT 'PENDING',
  notes text NULL,
  applied_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE nutrient.nutrient_budget (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  nutrient_code varchar(40) NOT NULL,
  planned_quantity numeric(18,4) NOT NULL DEFAULT 0,
  applied_quantity numeric(18,4) NOT NULL DEFAULT 0,
  estimated_crop_uptake numeric(18,4) NULL,
  estimated_balance numeric(18,4) NULL,
  quantity_unit varchar(40) NOT NULL,
  calculated_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL,
  CONSTRAINT uq_nutrient_budget_cycle_code UNIQUE (crop_cycle_id, nutrient_code)
);

CREATE INDEX ix_nutrient_plan_cycle_status
  ON nutrient.nutrient_plan(crop_cycle_id, status, created_at DESC);

CREATE INDEX ix_nutrient_application_cycle_time
  ON nutrient.nutrient_application(crop_cycle_id, applied_at DESC);

CREATE TRIGGER trg_nutrient_plan_updated_at
BEFORE UPDATE ON nutrient.nutrient_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
