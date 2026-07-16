CREATE TABLE organization.collective_collection_lot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  collection_code varchar(120) NOT NULL,
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  grade_code varchar(40) NULL,
  total_quantity numeric(18,4) NOT NULL DEFAULT 0,
  quantity_unit varchar(40) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','COLLECTING','READY_FOR_SALE','LISTED','SOLD','SETTLED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_collection_lot_code UNIQUE (organization_id, collection_code)
);

CREATE TABLE organization.collective_collection_contribution (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  collection_lot_id uuid NOT NULL REFERENCES organization.collective_collection_lot(id) ON DELETE CASCADE,
  membership_id uuid NOT NULL REFERENCES organization.organization_membership(id),
  trace_lot_id uuid NULL REFERENCES traceability.trace_lot(id),
  contributed_quantity numeric(18,4) NOT NULL CHECK (contributed_quantity > 0),
  accepted_quantity numeric(18,4) NULL,
  rejected_quantity numeric(18,4) NOT NULL DEFAULT 0,
  quality_grade varchar(40) NULL,
  contribution_value numeric(18,2) NULL,
  status varchar(30) NOT NULL DEFAULT 'RECEIVED'
    CHECK (status IN ('RECEIVED','ACCEPTED','PARTIALLY_ACCEPTED','REJECTED','SETTLED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE organization.collective_sale (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  collection_lot_id uuid NOT NULL REFERENCES organization.collective_collection_lot(id),
  buyer_id uuid NULL REFERENCES market.buyer_profile(id),
  sale_reference varchar(140) NOT NULL,
  sold_quantity numeric(18,4) NOT NULL CHECK (sold_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  unit_price numeric(18,4) NOT NULL,
  gross_value numeric(18,2) NOT NULL,
  deductions numeric(18,2) NOT NULL DEFAULT 0,
  net_value numeric(18,2) NOT NULL,
  sale_date date NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'CONFIRMED'
    CHECK (status IN ('DRAFT','CONFIRMED','DISPATCHED','DELIVERED','PAID','SETTLED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_collective_sale_reference UNIQUE (organization_id, sale_reference)
);

CREATE TABLE organization.collective_sale_settlement (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  collective_sale_id uuid NOT NULL REFERENCES organization.collective_sale(id),
  membership_id uuid NOT NULL REFERENCES organization.organization_membership(id),
  contribution_id uuid NOT NULL REFERENCES organization.collective_collection_contribution(id),
  gross_share numeric(18,2) NOT NULL,
  deduction_share numeric(18,2) NOT NULL DEFAULT 0,
  net_payable numeric(18,2) NOT NULL,
  paid_amount numeric(18,2) NOT NULL DEFAULT 0,
  status varchar(30) NOT NULL DEFAULT 'PENDING'
    CHECK (status IN ('PENDING','APPROVED','PARTIALLY_PAID','PAID','ON_HOLD','DISPUTED')),
  financial_event_id uuid NULL REFERENCES finance.financial_event(id),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_collective_sale_member_settlement UNIQUE (collective_sale_id, membership_id, contribution_id)
);

CREATE TABLE organization.organization_financial_entry (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  entry_date date NOT NULL,
  entry_type varchar(60) NOT NULL
    CHECK (entry_type IN ('MEMBERSHIP_FEE','SHARE_CAPITAL','PROCUREMENT_PAYMENT','RESOURCE_INCOME','COLLECTIVE_SALE','OPERATING_EXPENSE','DIVIDEND','OTHER')),
  direction varchar(20) NOT NULL
    CHECK (direction IN ('INFLOW','OUTFLOW','NON_CASH')),
  amount numeric(18,2) NOT NULL CHECK (amount >= 0),
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  reference_type varchar(100) NULL,
  reference_id uuid NULL,
  description varchar(500) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_collection_lot_status
  ON organization.collective_collection_lot(organization_id, status, created_at DESC);

CREATE INDEX ix_collective_sale_status
  ON organization.collective_sale(organization_id, status, sale_date DESC);

CREATE INDEX ix_sale_settlement_status
  ON organization.collective_sale_settlement(collective_sale_id, status);

CREATE INDEX ix_org_financial_entry_date
  ON organization.organization_financial_entry(organization_id, entry_date DESC);
