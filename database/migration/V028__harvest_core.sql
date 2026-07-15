CREATE TABLE harvest.harvest_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  expected_start_date date NOT NULL,
  expected_end_date date NOT NULL,
  harvest_method varchar(40) NOT NULL
    CHECK (harvest_method IN ('MANUAL','MECHANICAL','MIXED')),
  expected_yield_quantity numeric(18,4) NULL,
  expected_yield_unit varchar(40) NULL,
  readiness_score numeric(8,4) NULL,
  weather_suitability varchar(30) NOT NULL DEFAULT 'NOT_ASSESSED',
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','PLANNED','APPROVED','READY','IN_PROGRESS','COMPLETED','CANCELLED')),
  notes text NULL,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_harvest_window CHECK (expected_end_date >= expected_start_date)
);

CREATE TABLE harvest.harvest_batch (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  harvest_plan_id uuid NOT NULL REFERENCES harvest.harvest_plan(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  batch_code varchar(120) NOT NULL,
  harvested_at timestamptz NOT NULL,
  harvest_method varchar(40) NOT NULL,
  gross_quantity numeric(18,4) NOT NULL CHECK (gross_quantity > 0),
  tare_quantity numeric(18,4) NOT NULL DEFAULT 0 CHECK (tare_quantity >= 0),
  net_quantity numeric(18,4) NOT NULL CHECK (net_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  moisture_percent numeric(8,4) NULL,
  damaged_quantity numeric(18,4) NOT NULL DEFAULT 0,
  field_loss_quantity numeric(18,4) NOT NULL DEFAULT 0,
  transport_loss_quantity numeric(18,4) NOT NULL DEFAULT 0,
  status varchar(30) NOT NULL DEFAULT 'HARVESTED'
    CHECK (status IN ('HARVESTED','RECEIVED','UNDER_QUALITY_CHECK','GRADED','PACKED','STORED','DISPATCHED','CLOSED','REJECTED')),
  notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_harvest_batch_code UNIQUE (tenant_id, batch_code),
  CONSTRAINT ck_harvest_batch_net CHECK (net_quantity = gross_quantity - tare_quantity)
);

CREATE TABLE harvest.harvest_loss (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  harvest_batch_id uuid NOT NULL REFERENCES harvest.harvest_batch(id) ON DELETE CASCADE,
  loss_type varchar(60) NOT NULL,
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  reason_code varchar(80) NULL,
  notes text NULL,
  recorded_at timestamptz NOT NULL DEFAULT now(),
  recorded_by uuid NULL
);

CREATE INDEX ix_harvest_plan_cycle_status
  ON harvest.harvest_plan(crop_cycle_id, status, expected_start_date);

CREATE INDEX ix_harvest_batch_cycle_time
  ON harvest.harvest_batch(crop_cycle_id, harvested_at DESC);

CREATE TRIGGER trg_harvest_plan_updated_at
BEFORE UPDATE ON harvest.harvest_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_harvest_batch_updated_at
BEFORE UPDATE ON harvest.harvest_batch
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
