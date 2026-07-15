CREATE TABLE market.sales_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  trace_lot_id uuid NULL REFERENCES traceability.trace_lot(id),
  plan_name varchar(240) NOT NULL,
  available_quantity numeric(18,4) NOT NULL CHECK (available_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  minimum_acceptable_price numeric(18,4) NULL,
  target_price numeric(18,4) NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  sales_window_start date NOT NULL,
  sales_window_end date NOT NULL,
  strategy varchar(60) NOT NULL
    CHECK (strategy IN ('IMMEDIATE_SALE','STAGGERED_SALE','STORE_AND_SELL','FORWARD_CONTRACT','DIRECT_CONSUMER','PROCESSOR','EXPORT')),
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','APPROVED','ACTIVE','PARTIALLY_SOLD','COMPLETED','CANCELLED')),
  recommendation_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_sales_window CHECK (sales_window_end >= sales_window_start)
);

CREATE TABLE market.buyer_profile (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  buyer_code varchar(100) NOT NULL,
  buyer_name varchar(240) NOT NULL,
  buyer_type varchar(60) NOT NULL
    CHECK (buyer_type IN ('TRADER','PROCESSOR','RETAILER','WHOLESALER','COOPERATIVE','EXPORTER','INSTITUTION','DIRECT_CUSTOMER')),
  mobile varchar(30) NULL,
  email varchar(240) NULL,
  gstin varchar(30) NULL,
  address_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  rating numeric(8,4) NULL,
  payment_reliability_score numeric(8,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_buyer_code UNIQUE (tenant_id, buyer_code)
);

CREATE TABLE market.buyer_offer (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  sales_plan_id uuid NOT NULL REFERENCES market.sales_plan(id),
  buyer_id uuid NOT NULL REFERENCES market.buyer_profile(id),
  offered_at timestamptz NOT NULL,
  offered_quantity numeric(18,4) NOT NULL CHECK (offered_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  offered_price numeric(18,4) NOT NULL CHECK (offered_price >= 0),
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  pickup_terms varchar(80) NULL,
  payment_terms varchar(120) NULL,
  valid_until timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','SHORTLISTED','ACCEPTED','REJECTED','EXPIRED','WITHDRAWN')),
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE market.sale_contract (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  sales_plan_id uuid NOT NULL REFERENCES market.sales_plan(id),
  buyer_offer_id uuid NULL REFERENCES market.buyer_offer(id),
  buyer_id uuid NOT NULL REFERENCES market.buyer_profile(id),
  contract_number varchar(120) NOT NULL,
  contracted_quantity numeric(18,4) NOT NULL CHECK (contracted_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  unit_price numeric(18,4) NOT NULL CHECK (unit_price >= 0),
  total_value numeric(18,2) NOT NULL CHECK (total_value >= 0),
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  delivery_date date NULL,
  payment_due_date date NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','CONFIRMED','PARTIALLY_FULFILLED','FULFILLED','CANCELLED','DISPUTED','CLOSED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_sale_contract_number UNIQUE (tenant_id, contract_number)
);

CREATE INDEX ix_sales_plan_cycle_status
  ON market.sales_plan(crop_cycle_id, status, sales_window_start);

CREATE INDEX ix_buyer_offer_plan
  ON market.buyer_offer(sales_plan_id, status, offered_price DESC);

CREATE INDEX ix_sale_contract_buyer_status
  ON market.sale_contract(buyer_id, status, created_at DESC);

CREATE TRIGGER trg_sales_plan_updated_at
BEFORE UPDATE ON market.sales_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
