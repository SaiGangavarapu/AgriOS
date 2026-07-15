CREATE TABLE organization.farmer_organization (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_code varchar(100) NOT NULL,
  organization_name varchar(240) NOT NULL,
  organization_type varchar(60) NOT NULL
    CHECK (organization_type IN ('FPO','COOPERATIVE','SHG','PRODUCER_COMPANY','FARMER_GROUP','CLUSTER')),
  registration_number varchar(160) NULL,
  registration_authority varchar(240) NULL,
  registration_date date NULL,
  primary_geography_code varchar(80) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','SUSPENDED','INACTIVE','DISSOLVED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_farmer_organization_code UNIQUE (tenant_id, organization_code)
);

CREATE TABLE organization.organization_membership (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  membership_number varchar(120) NOT NULL,
  membership_type varchar(50) NOT NULL
    CHECK (membership_type IN ('FOUNDING','ORDINARY','ASSOCIATE','INSTITUTIONAL','HONORARY')),
  joined_on date NOT NULL,
  share_units numeric(18,4) NOT NULL DEFAULT 0,
  share_value numeric(18,2) NOT NULL DEFAULT 0,
  status varchar(30) NOT NULL DEFAULT 'PENDING'
    CHECK (status IN ('PENDING','ACTIVE','SUSPENDED','RESIGNED','TERMINATED','REJECTED')),
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_org_farmer_membership UNIQUE (organization_id, farmer_id),
  CONSTRAINT uq_org_membership_number UNIQUE (organization_id, membership_number)
);

CREATE TABLE organization.governance_role (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  membership_id uuid NOT NULL REFERENCES organization.organization_membership(id),
  role_code varchar(80) NOT NULL,
  role_name varchar(160) NOT NULL,
  valid_from date NOT NULL,
  valid_until date NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','EXPIRED','REVOKED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE organization.organization_meeting (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  organization_id uuid NOT NULL REFERENCES organization.farmer_organization(id),
  meeting_type varchar(60) NOT NULL
    CHECK (meeting_type IN ('GENERAL_BODY','BOARD','COMMITTEE','PROCUREMENT','MARKETING','EMERGENCY')),
  title varchar(240) NOT NULL,
  scheduled_at timestamptz NOT NULL,
  location varchar(300) NULL,
  agenda text NULL,
  minutes text NULL,
  status varchar(30) NOT NULL DEFAULT 'SCHEDULED'
    CHECK (status IN ('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_org_membership_status
  ON organization.organization_membership(organization_id, status, joined_on);

CREATE INDEX ix_org_meeting_status
  ON organization.organization_meeting(organization_id, status, scheduled_at);

CREATE TRIGGER trg_farmer_organization_updated_at
BEFORE UPDATE ON organization.farmer_organization
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_organization_meeting_updated_at
BEFORE UPDATE ON organization.organization_meeting
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
