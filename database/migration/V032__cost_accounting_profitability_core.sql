CREATE TABLE finance.cost_center (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  name varchar(200) NOT NULL,
  category varchar(80) NOT NULL,
  active boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_cost_center_code UNIQUE (tenant_id, code)
);

CREATE TABLE finance.cost_allocation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  financial_event_id uuid NOT NULL REFERENCES finance.financial_event(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  cost_center_id uuid NULL REFERENCES finance.cost_center(id),
  allocation_percent numeric(8,4) NOT NULL
    CHECK (allocation_percent > 0 AND allocation_percent <= 100),
  allocated_amount numeric(18,2) NOT NULL CHECK (allocated_amount >= 0),
  allocation_basis varchar(80) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE finance.budget_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NULL REFERENCES farm.field(id),
  budget_name varchar(200) NOT NULL,
  period_start date NOT NULL,
  period_end date NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','APPROVED','ACTIVE','CLOSED','CANCELLED')),
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  version bigint NOT NULL DEFAULT 0,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_budget_period CHECK (period_end >= period_start)
);

CREATE TABLE finance.budget_line (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  budget_plan_id uuid NOT NULL REFERENCES finance.budget_plan(id) ON DELETE CASCADE,
  cost_center_id uuid NULL REFERENCES finance.cost_center(id),
  income_category varchar(80) NULL,
  planned_amount numeric(18,2) NOT NULL CHECK (planned_amount >= 0),
  actual_amount numeric(18,2) NOT NULL DEFAULT 0 CHECK (actual_amount >= 0),
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE finance.profitability_snapshot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  period_start date NOT NULL,
  period_end date NOT NULL,
  revenue numeric(18,2) NOT NULL DEFAULT 0,
  operating_cost numeric(18,2) NOT NULL DEFAULT 0,
  allocated_overhead numeric(18,2) NOT NULL DEFAULT 0,
  depreciation numeric(18,2) NOT NULL DEFAULT 0,
  finance_cost numeric(18,2) NOT NULL DEFAULT 0,
  net_profit numeric(18,2) NOT NULL DEFAULT 0,
  roi_percent numeric(12,4) NULL,
  cost_per_unit numeric(18,4) NULL,
  production_quantity numeric(18,4) NULL,
  production_unit varchar(40) NULL,
  calculation_version varchar(60) NOT NULL DEFAULT 'v1',
  calculated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_profitability_snapshot UNIQUE (
    tenant_id, farmer_id, field_id, crop_cycle_id, period_start, period_end
  )
);

CREATE INDEX ix_cost_allocation_cycle
  ON finance.cost_allocation(crop_cycle_id, cost_center_id);

CREATE INDEX ix_budget_farmer_period
  ON finance.budget_plan(farmer_id, period_start, period_end);

CREATE INDEX ix_profitability_farmer_period
  ON finance.profitability_snapshot(farmer_id, period_start DESC, period_end DESC);

CREATE TRIGGER trg_budget_plan_updated_at
BEFORE UPDATE ON finance.budget_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
