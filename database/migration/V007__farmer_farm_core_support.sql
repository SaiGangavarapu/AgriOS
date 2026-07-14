CREATE TABLE IF NOT EXISTS household.household (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  name varchar(200) NOT NULL,
  primary_farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE IF NOT EXISTS household.household_member (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES household.household(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  user_account_id uuid NULL REFERENCES identity.user_account(id),
  role_code varchar(60) NOT NULL,
  valid_from date NOT NULL DEFAULT current_date,
  valid_to date NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_household_member_subject CHECK (
    farmer_id IS NOT NULL OR user_account_id IS NOT NULL
  )
);

CREATE TABLE IF NOT EXISTS organization.organization (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  name varchar(200) NOT NULL,
  organization_type varchar(60) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_organization_tenant_code UNIQUE (tenant_id, code)
);

CREATE TABLE IF NOT EXISTS organization.organization_member (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  organization_id uuid NOT NULL REFERENCES organization.organization(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  user_account_id uuid NULL REFERENCES identity.user_account(id),
  role_code varchar(60) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  valid_from date NOT NULL DEFAULT current_date,
  valid_to date NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_organization_member_subject CHECK (
    farmer_id IS NOT NULL OR user_account_id IS NOT NULL
  )
);

CREATE TABLE IF NOT EXISTS farm.farm_participant (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  farm_id uuid NOT NULL REFERENCES farm.farm(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  role_code varchar(60) NOT NULL,
  is_primary boolean NOT NULL DEFAULT false,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  valid_from date NOT NULL DEFAULT current_date,
  valid_to date NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS ix_household_primary_farmer
  ON household.household(tenant_id, primary_farmer_id);

CREATE INDEX IF NOT EXISTS ix_organization_member_farmer
  ON organization.organization_member(farmer_id)
  WHERE farmer_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_farm_participant_farmer
  ON farm.farm_participant(farmer_id, status);

INSERT INTO configuration.tenant (
  id, code, name, status, default_language, timezone, country_code
)
VALUES (
  '00000000-0000-0000-0000-000000000001',
  'local-dev',
  'AgriOS Local Development',
  'ACTIVE',
  'en',
  'Asia/Kolkata',
  'IN'
)
ON CONFLICT (id) DO NOTHING;
