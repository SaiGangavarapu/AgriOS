CREATE TABLE finance.farm_asset (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  asset_code varchar(100) NOT NULL,
  asset_name varchar(200) NOT NULL,
  asset_category varchar(80) NOT NULL,
  purchase_date date NULL,
  purchase_cost numeric(18,2) NULL,
  salvage_value numeric(18,2) NULL,
  useful_life_years integer NULL,
  depreciation_method varchar(40) NOT NULL DEFAULT 'STRAIGHT_LINE',
  current_book_value numeric(18,2) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','UNDER_MAINTENANCE','DISPOSED','LOST','RETIRED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_farm_asset_code UNIQUE (tenant_id, asset_code)
);

CREATE TABLE finance.loan_account (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  lender_name varchar(240) NOT NULL,
  loan_reference varchar(160) NULL,
  principal_amount numeric(18,2) NOT NULL CHECK (principal_amount > 0),
  annual_interest_rate numeric(10,4) NOT NULL CHECK (annual_interest_rate >= 0),
  disbursement_date date NOT NULL,
  maturity_date date NULL,
  outstanding_principal numeric(18,2) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('APPLIED','APPROVED','ACTIVE','CLOSED','DEFAULTED','REJECTED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE finance.subsidy_application (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  scheme_code varchar(120) NOT NULL,
  scheme_name varchar(240) NOT NULL,
  application_reference varchar(160) NULL,
  applied_amount numeric(18,2) NULL,
  approved_amount numeric(18,2) NULL,
  received_amount numeric(18,2) NULL,
  applied_at date NULL,
  received_at date NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','APPLIED','UNDER_REVIEW','APPROVED','PARTIALLY_RECEIVED','RECEIVED','REJECTED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE finance.insurance_policy (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  insurer_name varchar(240) NOT NULL,
  policy_number varchar(160) NOT NULL,
  policy_type varchar(80) NOT NULL,
  premium_amount numeric(18,2) NOT NULL DEFAULT 0,
  sum_insured numeric(18,2) NOT NULL DEFAULT 0,
  valid_from date NOT NULL,
  valid_until date NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','EXPIRED','CANCELLED','CLAIMED','CLOSED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_insurance_policy_number UNIQUE (tenant_id, policy_number),
  CONSTRAINT ck_insurance_validity CHECK (valid_until >= valid_from)
);

CREATE TABLE finance.income_aggregation_snapshot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  period_start date NOT NULL,
  period_end date NOT NULL,
  crop_income numeric(18,2) NOT NULL DEFAULT 0,
  livestock_income numeric(18,2) NOT NULL DEFAULT 0,
  dairy_income numeric(18,2) NOT NULL DEFAULT 0,
  marketplace_income numeric(18,2) NOT NULL DEFAULT 0,
  seller_erp_income numeric(18,2) NOT NULL DEFAULT 0,
  subsidy_income numeric(18,2) NOT NULL DEFAULT 0,
  insurance_income numeric(18,2) NOT NULL DEFAULT 0,
  other_income numeric(18,2) NOT NULL DEFAULT 0,
  total_income numeric(18,2) NOT NULL DEFAULT 0,
  calculated_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT uq_income_aggregation UNIQUE (tenant_id, farmer_id, period_start, period_end)
);

CREATE INDEX ix_asset_farmer_status
  ON finance.farm_asset(farmer_id, status);

CREATE INDEX ix_loan_farmer_status
  ON finance.loan_account(farmer_id, status);

CREATE INDEX ix_subsidy_farmer_status
  ON finance.subsidy_application(farmer_id, status);

CREATE INDEX ix_income_aggregation_farmer
  ON finance.income_aggregation_snapshot(farmer_id, period_start DESC, period_end DESC);
