CREATE TABLE operations.farm_operation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  operation_type varchar(80) NOT NULL,
  operation_date date NOT NULL,
  started_at timestamptz NULL,
  completed_at timestamptz NULL,
  area_hectares numeric(12,4) NULL CHECK (area_hectares IS NULL OR area_hectares > 0),
  status varchar(30) NOT NULL DEFAULT 'PLANNED'
    CHECK (status IN ('PLANNED','IN_PROGRESS','COMPLETED','SKIPPED','CANCELLED','CORRECTED')),
  notes text NULL,
  performed_by uuid NULL,
  source_task_id uuid NULL,
  correction_of_operation_id uuid NULL REFERENCES operations.farm_operation(id),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE operations.operation_input (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  operation_id uuid NOT NULL REFERENCES operations.farm_operation(id) ON DELETE CASCADE,
  input_category varchar(60) NOT NULL,
  product_name varchar(200) NOT NULL,
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  unit_code varchar(40) NOT NULL,
  batch_reference varchar(120) NULL,
  cost_amount numeric(14,2) NULL,
  currency varchar(3) NOT NULL DEFAULT 'INR',
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE operations.labour_usage (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  operation_id uuid NOT NULL REFERENCES operations.farm_operation(id) ON DELETE CASCADE,
  labour_type varchar(60) NOT NULL,
  worker_count integer NOT NULL CHECK (worker_count > 0),
  hours_per_worker numeric(10,2) NOT NULL CHECK (hours_per_worker >= 0),
  cost_amount numeric(14,2) NULL,
  currency varchar(3) NOT NULL DEFAULT 'INR',
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE operations.machinery_usage (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  operation_id uuid NOT NULL REFERENCES operations.farm_operation(id) ON DELETE CASCADE,
  machinery_type varchar(80) NOT NULL,
  machinery_reference varchar(120) NULL,
  usage_hours numeric(10,2) NOT NULL CHECK (usage_hours >= 0),
  fuel_quantity numeric(12,4) NULL,
  fuel_unit varchar(40) NULL,
  cost_amount numeric(14,2) NULL,
  currency varchar(3) NOT NULL DEFAULT 'INR',
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_operation_cycle_date
  ON operations.farm_operation(crop_cycle_id, operation_date DESC);

CREATE INDEX ix_operation_status_date
  ON operations.farm_operation(tenant_id, status, operation_date);

CREATE TRIGGER trg_farm_operation_updated_at
BEFORE UPDATE ON operations.farm_operation
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
