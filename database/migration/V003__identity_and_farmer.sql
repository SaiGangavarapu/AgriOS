CREATE TABLE identity.user_account (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  username citext NULL,
  mobile_e164 varchar(20) NULL,
  email citext NULL,
  password_hash varchar(255) NULL,
  status varchar(30) NOT NULL CHECK (status IN ('PENDING','ACTIVE','LOCKED','SUSPENDED','ARCHIVED')),
  preferred_language varchar(10) NOT NULL DEFAULT 'en',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_user_login_identifier CHECK (
    username IS NOT NULL OR mobile_e164 IS NOT NULL OR email IS NOT NULL
  )
);

CREATE UNIQUE INDEX uq_user_tenant_mobile
  ON identity.user_account(tenant_id, mobile_e164)
  WHERE mobile_e164 IS NOT NULL;

CREATE UNIQUE INDEX uq_user_tenant_email
  ON identity.user_account(tenant_id, email)
  WHERE email IS NOT NULL;

CREATE TABLE farmer.farmer (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  programme_id uuid NULL REFERENCES configuration.programme(id),
  linked_user_account_id uuid NULL REFERENCES identity.user_account(id),
  full_name varchar(200) NOT NULL,
  preferred_name varchar(120) NULL,
  preferred_language varchar(10) NOT NULL DEFAULT 'en',
  mobile_e164 varchar(20) NULL,
  village_name varchar(160) NOT NULL,
  district_name varchar(160) NULL,
  state_name varchar(160) NULL,
  verification_level varchar(40) NOT NULL DEFAULT 'UNVERIFIED'
    CHECK (verification_level IN ('UNVERIFIED','CONTACT_VERIFIED','INSTITUTION_VERIFIED','DOCUMENT_VERIFIED')),
  status varchar(40) NOT NULL DEFAULT 'REGISTERED'
    CHECK (status IN ('DRAFT','REGISTERED','ACTIVE','SUSPENDED','DUPLICATE_UNDER_REVIEW','MERGED','ARCHIVED','DECEASED','WITHDRAWN')),
  canonical_farmer_id uuid NULL,
  source_channel varchar(40) NOT NULL DEFAULT 'SELF',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT fk_farmer_canonical FOREIGN KEY (canonical_farmer_id) REFERENCES farmer.farmer(id),
  CONSTRAINT ck_farmer_merged_canonical CHECK (
    status <> 'MERGED' OR canonical_farmer_id IS NOT NULL
  )
);

CREATE TABLE farmer.farmer_verification (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  verification_level varchar(40) NOT NULL,
  evidence_type varchar(80) NOT NULL,
  evidence_reference varchar(255) NULL,
  verified_by uuid NULL,
  verified_at timestamptz NOT NULL DEFAULT now(),
  expires_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'VALID',
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TRIGGER trg_user_updated_at
BEFORE UPDATE ON identity.user_account
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_farmer_updated_at
BEFORE UPDATE ON farmer.farmer
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
