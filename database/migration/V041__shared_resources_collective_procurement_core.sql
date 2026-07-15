CREATE TABLE organization.shared_resource (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  resource_code varchar(100) NOT NULL,
  resource_name varchar(240) NOT NULL,
  resource_type varchar(60) NOT NULL
    CHECK (resource_type IN ('TRACTOR','ROTAVATOR','HARVESTER','DRONE','SPRAYER','SEEDER','WAREHOUSE','COLD_STORAGE','WATER_TANKER','LABOUR_POOL','OTHER')),
  capacity numeric(18,4) NULL,
  capacity_unit varchar(40) NULL,
  hourly_rate numeric(18,2) NULL,
  daily_rate numeric(18,2) NULL,
  status varchar(30) NOT NULL DEFAULT 'AVAILABLE'
    CHECK (status IN ('AVAILABLE','BOOKED','IN_USE','MAINTENANCE','OUT_OF_SERVICE','RETIRED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_shared_resource_code UNIQUE (organization_id, resource_code)
);

CREATE TABLE organization.resource_booking (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  resource_id uuid NOT NULL REFERENCES organization.shared_resource(id),
  membership_id uuid NOT NULL REFERENCES organization.organization_membership(id),
  booking_start timestamptz NOT NULL,
  booking_end timestamptz NOT NULL,
  purpose varchar(300) NOT NULL,
  estimated_cost numeric(18,2) NOT NULL DEFAULT 0,
  actual_cost numeric(18,2) NULL,
  status varchar(30) NOT NULL DEFAULT 'REQUESTED'
    CHECK (status IN ('REQUESTED','APPROVED','REJECTED','IN_PROGRESS','COMPLETED','CANCELLED')),
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_resource_booking_window CHECK (booking_end > booking_start)
);

CREATE TABLE organization.procurement_request (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  request_code varchar(120) NOT NULL,
  item_category varchar(80) NOT NULL,
  item_name varchar(240) NOT NULL,
  target_quantity numeric(18,4) NOT NULL CHECK (target_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  required_by date NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','AGGREGATING','QUOTATION','APPROVED','ORDERED','RECEIVED','CLOSED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_procurement_request_code UNIQUE (organization_id, request_code)
);

CREATE TABLE organization.procurement_demand (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  procurement_request_id uuid NOT NULL REFERENCES organization.procurement_request(id) ON DELETE CASCADE,
  membership_id uuid NOT NULL REFERENCES organization.organization_membership(id),
  requested_quantity numeric(18,4) NOT NULL CHECK (requested_quantity > 0),
  allocated_quantity numeric(18,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'REQUESTED'
    CHECK (status IN ('REQUESTED','CONFIRMED','ALLOCATED','FULFILLED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_procurement_member_demand UNIQUE (procurement_request_id, membership_id)
);

CREATE TABLE organization.vendor_quotation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  procurement_request_id uuid NOT NULL REFERENCES organization.procurement_request(id),
  vendor_name varchar(240) NOT NULL,
  quoted_quantity numeric(18,4) NOT NULL,
  unit_price numeric(18,4) NOT NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  delivery_days integer NULL,
  validity_until date NULL,
  status varchar(30) NOT NULL DEFAULT 'RECEIVED'
    CHECK (status IN ('RECEIVED','SHORTLISTED','SELECTED','REJECTED','EXPIRED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_resource_booking_window
  ON organization.resource_booking(resource_id, booking_start, booking_end, status);

CREATE INDEX ix_procurement_request_status
  ON organization.procurement_request(organization_id, status, required_by);

CREATE TRIGGER trg_shared_resource_updated_at
BEFORE UPDATE ON organization.shared_resource
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
