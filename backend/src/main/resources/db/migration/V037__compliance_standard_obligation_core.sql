CREATE SCHEMA IF NOT EXISTS compliance;

CREATE TABLE compliance.standard_catalog (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  standard_code varchar(100) NOT NULL,
  standard_name varchar(240) NOT NULL,
  standard_type varchar(80) NOT NULL
    CHECK (standard_type IN ('ORGANIC','GAP','FOOD_SAFETY','EXPORT','ENVIRONMENT','LABOUR','LOCAL_REGULATION','QUALITY')),
  issuing_authority varchar(240) NULL,
  jurisdiction_code varchar(80) NULL,
  version_label varchar(80) NULL,
  effective_from date NULL,
  effective_until date NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','SUPERSEDED','WITHDRAWN')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_standard_catalog_code UNIQUE (tenant_id, standard_code, version_label)
);

CREATE TABLE compliance.requirement_catalog (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  standard_id uuid NOT NULL REFERENCES compliance.standard_catalog(id) ON DELETE CASCADE,
  requirement_code varchar(120) NOT NULL,
  requirement_title varchar(300) NOT NULL,
  requirement_description text NULL,
  requirement_category varchar(100) NOT NULL,
  evidence_type varchar(80) NULL,
  mandatory boolean NOT NULL DEFAULT true,
  severity_if_failed varchar(20) NOT NULL DEFAULT 'MAJOR'
    CHECK (severity_if_failed IN ('MINOR','MAJOR','CRITICAL')),
  control_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_requirement_catalog UNIQUE (standard_id, requirement_code)
);

CREATE TABLE compliance.compliance_profile (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  profile_name varchar(240) NOT NULL,
  profile_type varchar(60) NOT NULL
    CHECK (profile_type IN ('FARM','FARMER','PROCESSOR','SELLER','WAREHOUSE','GROUP')),
  geography_code varchar(80) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE compliance.compliance_obligation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  compliance_profile_id uuid NOT NULL REFERENCES compliance.compliance_profile(id),
  standard_id uuid NOT NULL REFERENCES compliance.standard_catalog(id),
  obligation_status varchar(30) NOT NULL DEFAULT 'APPLICABLE'
    CHECK (obligation_status IN ('APPLICABLE','IN_PROGRESS','COMPLIANT','NON_COMPLIANT','EXEMPT','EXPIRED')),
  applicable_from date NOT NULL,
  applicable_until date NULL,
  exemption_reason text NULL,
  last_assessed_at timestamptz NULL,
  next_assessment_due date NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_compliance_obligation UNIQUE (compliance_profile_id, standard_id, applicable_from)
);

CREATE INDEX ix_compliance_obligation_profile
  ON compliance.compliance_obligation(compliance_profile_id, obligation_status, next_assessment_due);

CREATE TRIGGER trg_standard_catalog_updated_at
BEFORE UPDATE ON compliance.standard_catalog
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_compliance_profile_updated_at
BEFORE UPDATE ON compliance.compliance_profile
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
