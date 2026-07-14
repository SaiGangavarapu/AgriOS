CREATE TABLE configuration.tenant (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  code varchar(80) NOT NULL UNIQUE,
  name varchar(200) NOT NULL,
  status varchar(30) NOT NULL CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED')),
  default_language varchar(10) NOT NULL DEFAULT 'en',
  timezone varchar(80) NOT NULL DEFAULT 'Asia/Kolkata',
  country_code char(2) NOT NULL DEFAULT 'IN',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE configuration.programme (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  name varchar(200) NOT NULL,
  description text NULL,
  valid_from date NULL,
  valid_to date NULL,
  status varchar(30) NOT NULL CHECK (status IN ('DRAFT','ACTIVE','INACTIVE','CLOSED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_programme_tenant_code UNIQUE (tenant_id, code),
  CONSTRAINT ck_programme_dates CHECK (valid_to IS NULL OR valid_from IS NULL OR valid_to >= valid_from)
);

CREATE TRIGGER trg_tenant_updated_at
BEFORE UPDATE ON configuration.tenant
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_programme_updated_at
BEFORE UPDATE ON configuration.programme
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
