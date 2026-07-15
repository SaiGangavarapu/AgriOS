CREATE TABLE finance.financial_account (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  account_code varchar(80) NOT NULL,
  account_name varchar(200) NOT NULL,
  account_type varchar(30) NOT NULL
    CHECK (account_type IN ('ASSET','LIABILITY','EQUITY','INCOME','EXPENSE')),
  parent_account_id uuid NULL REFERENCES finance.financial_account(id),
  system_account boolean NOT NULL DEFAULT false,
  active boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_financial_account_code UNIQUE (tenant_id, account_code)
);

CREATE TABLE finance.financial_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  event_type varchar(100) NOT NULL,
  source_module varchar(80) NOT NULL,
  source_reference_type varchar(100) NOT NULL,
  source_reference_id uuid NULL,
  event_date date NOT NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  amount numeric(18,2) NOT NULL CHECK (amount >= 0),
  direction varchar(20) NOT NULL
    CHECK (direction IN ('INFLOW','OUTFLOW','NON_CASH')),
  description varchar(500) NOT NULL,
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  cost_center_code varchar(80) NULL,
  idempotency_key varchar(180) NOT NULL,
  payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'RECEIVED'
    CHECK (status IN ('RECEIVED','VALIDATED','POSTED','REJECTED','REVERSED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  posted_at timestamptz NULL,
  CONSTRAINT uq_financial_event_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE finance.journal_entry (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  financial_event_id uuid NULL REFERENCES finance.financial_event(id),
  journal_number varchar(100) NOT NULL,
  entry_date date NOT NULL,
  description varchar(500) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','POSTED','REVERSED')),
  reversal_of_id uuid NULL REFERENCES finance.journal_entry(id),
  posted_at timestamptz NULL,
  posted_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_journal_number UNIQUE (tenant_id, journal_number)
);

CREATE TABLE finance.journal_line (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  journal_entry_id uuid NOT NULL REFERENCES finance.journal_entry(id) ON DELETE CASCADE,
  financial_account_id uuid NOT NULL REFERENCES finance.financial_account(id),
  debit_amount numeric(18,2) NOT NULL DEFAULT 0 CHECK (debit_amount >= 0),
  credit_amount numeric(18,2) NOT NULL DEFAULT 0 CHECK (credit_amount >= 0),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  cost_center_code varchar(80) NULL,
  description varchar(500) NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_journal_line_side CHECK (
    (debit_amount > 0 AND credit_amount = 0)
    OR (credit_amount > 0 AND debit_amount = 0)
  )
);

CREATE INDEX ix_financial_event_farmer_date
  ON finance.financial_event(farmer_id, event_date DESC);

CREATE INDEX ix_financial_event_cycle
  ON finance.financial_event(crop_cycle_id, event_date DESC);

CREATE INDEX ix_journal_entry_date_status
  ON finance.journal_entry(tenant_id, entry_date DESC, status);

CREATE INDEX ix_journal_line_dimensions
  ON finance.journal_line(farmer_id, field_id, crop_cycle_id, cost_center_code);
