CREATE TABLE cropcycle.crop_cycle (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_plan_id uuid NOT NULL REFERENCES cropplanning.crop_plan(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  season_code varchar(60) NOT NULL,
  planned_area_hectares numeric(12,4) NOT NULL CHECK (planned_area_hectares > 0),
  status varchar(40) NOT NULL DEFAULT 'PLANNED'
    CHECK (status IN (
      'PLANNED','ACTIVATED','LAND_PREPARATION','SOWN','ESTABLISHED','GROWING',
      'REPRODUCTIVE','HARVEST_READY','HARVESTED','POST_HARVEST','CLOSED',
      'CANCELLED','FAILED_ESTABLISHMENT','RESOWING','PARTIAL_CROP_LOSS',
      'TOTAL_CROP_LOSS','ABANDONED'
    )),
  current_stage_code varchar(80) NULL,
  activated_at timestamptz NULL,
  sowing_date date NULL,
  closed_at timestamptz NULL,
  closure_notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_crop_cycle_plan UNIQUE (crop_plan_id)
);

CREATE TABLE cropcycle.stage_observation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id) ON DELETE CASCADE,
  stage_code varchar(80) NOT NULL,
  observed_at timestamptz NOT NULL,
  source_type varchar(40) NOT NULL,
  confidence_score numeric(8,4) NULL,
  notes text NULL,
  observed_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE cropcycle.crop_loss (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  loss_type varchar(40) NOT NULL,
  affected_area_hectares numeric(12,4) NOT NULL CHECK (affected_area_hectares > 0),
  estimated_loss_percent numeric(8,4) NULL
    CHECK (estimated_loss_percent IS NULL OR
           (estimated_loss_percent >= 0 AND estimated_loss_percent <= 100)),
  cause_code varchar(80) NOT NULL,
  observed_at timestamptz NOT NULL,
  notes text NULL,
  recorded_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE cropcycle.season_closure (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_cycle_id uuid NOT NULL UNIQUE REFERENCES cropcycle.crop_cycle(id),
  harvested_quantity numeric(18,4) NULL,
  harvested_unit varchar(40) NULL,
  marketable_quantity numeric(18,4) NULL,
  retained_seed_quantity numeric(18,4) NULL,
  household_use_quantity numeric(18,4) NULL,
  closing_notes text NULL,
  closed_at timestamptz NOT NULL,
  closed_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_crop_cycle_field_status
  ON cropcycle.crop_cycle(field_id, status, created_at DESC);

CREATE INDEX ix_stage_observation_cycle_time
  ON cropcycle.stage_observation(crop_cycle_id, observed_at DESC);

CREATE INDEX ix_crop_loss_cycle_time
  ON cropcycle.crop_loss(crop_cycle_id, observed_at DESC);

CREATE TRIGGER trg_crop_cycle_updated_at
BEFORE UPDATE ON cropcycle.crop_cycle
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
