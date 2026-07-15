CREATE TABLE compliance.scheme_catalog (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  scheme_code varchar(120) NOT NULL,
  scheme_name varchar(300) NOT NULL,
  scheme_type varchar(80) NOT NULL
    CHECK (scheme_type IN ('SUBSIDY','INSURANCE','CREDIT','TRAINING','EQUIPMENT','IRRIGATION','ORGANIC','MARKET_LINKAGE','INCOME_SUPPORT')),
  authority_name varchar(240) NOT NULL,
  geography_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  eligibility_rules jsonb NOT NULL DEFAULT '{}'::jsonb,
  benefit_definition jsonb NOT NULL DEFAULT '{}'::jsonb,
  application_url varchar(500) NULL,
  valid_from date NULL,
  valid_until date NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','SUSPENDED','EXPIRED','WITHDRAWN')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_scheme_catalog_code UNIQUE (tenant_id, scheme_code)
);

CREATE TABLE compliance.scheme_eligibility_assessment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  scheme_id uuid NOT NULL REFERENCES compliance.scheme_catalog(id),
  assessed_at timestamptz NOT NULL DEFAULT now(),
  eligibility_status varchar(30) NOT NULL
    CHECK (eligibility_status IN ('ELIGIBLE','POSSIBLY_ELIGIBLE','NOT_ELIGIBLE','INCOMPLETE_DATA')),
  eligibility_score numeric(8,4) NULL,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  missing_information jsonb NOT NULL DEFAULT '[]'::jsonb,
  evidence_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  assessment_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT uq_scheme_eligibility UNIQUE (farmer_id, scheme_id, assessment_version, assessed_at)
);

CREATE TABLE compliance.regulatory_report_definition (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  report_code varchar(120) NOT NULL,
  report_name varchar(300) NOT NULL,
  authority_name varchar(240) NOT NULL,
  jurisdiction_code varchar(80) NULL,
  reporting_frequency varchar(40) NOT NULL
    CHECK (reporting_frequency IN ('ON_DEMAND','MONTHLY','QUARTERLY','HALF_YEARLY','ANNUAL','EVENT_DRIVEN')),
  schema_definition jsonb NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_regulatory_report_code UNIQUE (tenant_id, report_code)
);

CREATE TABLE compliance.regulatory_report_submission (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  report_definition_id uuid NOT NULL REFERENCES compliance.regulatory_report_definition(id),
  compliance_profile_id uuid NULL REFERENCES compliance.compliance_profile(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  reporting_period_start date NULL,
  reporting_period_end date NULL,
  generated_at timestamptz NOT NULL DEFAULT now(),
  submitted_at timestamptz NULL,
  submission_reference varchar(180) NULL,
  payload jsonb NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'GENERATED'
    CHECK (status IN ('GENERATED','VALIDATED','SUBMITTED','ACCEPTED','REJECTED','CORRECTION_REQUIRED')),
  validation_errors jsonb NOT NULL DEFAULT '[]'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE compliance.audit_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  actor_id uuid NULL,
  actor_type varchar(60) NOT NULL,
  action_code varchar(120) NOT NULL,
  subject_type varchar(100) NOT NULL,
  subject_id uuid NULL,
  occurred_at timestamptz NOT NULL DEFAULT now(),
  before_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  after_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb
);

CREATE INDEX ix_scheme_eligibility_farmer
  ON compliance.scheme_eligibility_assessment(farmer_id, assessed_at DESC);

CREATE INDEX ix_regulatory_submission_status
  ON compliance.regulatory_report_submission(status, generated_at DESC);

CREATE INDEX ix_compliance_audit_subject
  ON compliance.audit_event(subject_type, subject_id, occurred_at DESC);

CREATE TRIGGER trg_scheme_catalog_updated_at
BEFORE UPDATE ON compliance.scheme_catalog
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
