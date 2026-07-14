CREATE TABLE irrigation.irrigation_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  water_source_id uuid NULL REFERENCES farm.water_source(id),
  water_profile_id uuid NULL REFERENCES soilwater.profile(id),
  irrigation_method varchar(60) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','GENERATED','APPROVED','ACTIVE','COMPLETED','CANCELLED','SUPERSEDED')),
  seasonal_water_requirement_mm numeric(14,4) NULL,
  planned_water_volume_m3 numeric(18,4) NULL,
  efficiency_percent numeric(8,4) NULL,
  recommendation_basis jsonb NOT NULL DEFAULT '{}'::jsonb,
  warnings jsonb NOT NULL DEFAULT '[]'::jsonb,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE irrigation.irrigation_schedule (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  irrigation_plan_id uuid NOT NULL REFERENCES irrigation.irrigation_plan(id) ON DELETE CASCADE,
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  scheduled_at timestamptz NOT NULL,
  target_depth_mm numeric(12,4) NOT NULL CHECK (target_depth_mm > 0),
  planned_volume_m3 numeric(18,4) NULL,
  crop_stage_code varchar(80) NULL,
  trigger_type varchar(60) NOT NULL,
  trigger_threshold numeric(18,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'SCHEDULED'
    CHECK (status IN ('SCHEDULED','DUE','IN_PROGRESS','EXECUTED','SKIPPED','DEFERRED','CANCELLED')),
  skip_reason text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE irrigation.irrigation_execution (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  irrigation_schedule_id uuid NOT NULL REFERENCES irrigation.irrigation_schedule(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  water_source_id uuid NULL REFERENCES farm.water_source(id),
  started_at timestamptz NOT NULL,
  completed_at timestamptz NULL,
  actual_depth_mm numeric(12,4) NULL,
  actual_volume_m3 numeric(18,4) NULL,
  pump_runtime_minutes integer NULL,
  energy_consumed_kwh numeric(14,4) NULL,
  execution_status varchar(30) NOT NULL DEFAULT 'STARTED'
    CHECK (execution_status IN ('STARTED','COMPLETED','PARTIAL','FAILED','CANCELLED')),
  notes text NULL,
  executed_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE irrigation.water_accounting (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  water_source_id uuid NULL REFERENCES farm.water_source(id),
  rainfall_effective_mm numeric(14,4) NOT NULL DEFAULT 0,
  irrigation_applied_mm numeric(14,4) NOT NULL DEFAULT 0,
  irrigation_volume_m3 numeric(18,4) NOT NULL DEFAULT 0,
  estimated_crop_demand_mm numeric(14,4) NULL,
  estimated_deficit_mm numeric(14,4) NULL,
  calculated_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL,
  CONSTRAINT uq_water_accounting_cycle_source UNIQUE (crop_cycle_id, water_source_id)
);

CREATE INDEX ix_irrigation_plan_cycle_status
  ON irrigation.irrigation_plan(crop_cycle_id, status, created_at DESC);

CREATE INDEX ix_irrigation_schedule_due
  ON irrigation.irrigation_schedule(crop_cycle_id, status, scheduled_at);

CREATE INDEX ix_irrigation_execution_cycle_time
  ON irrigation.irrigation_execution(crop_cycle_id, started_at DESC);

CREATE TRIGGER trg_irrigation_plan_updated_at
BEFORE UPDATE ON irrigation.irrigation_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_irrigation_schedule_updated_at
BEFORE UPDATE ON irrigation.irrigation_schedule
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
