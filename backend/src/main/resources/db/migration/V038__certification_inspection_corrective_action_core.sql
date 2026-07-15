CREATE TABLE compliance.certification_application (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  compliance_profile_id uuid NOT NULL REFERENCES compliance.compliance_profile(id),
  standard_id uuid NOT NULL REFERENCES compliance.standard_catalog(id),
  application_number varchar(140) NOT NULL,
  application_date date NOT NULL,
  certification_body varchar(240) NULL,
  requested_scope jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','SUBMITTED','UNDER_REVIEW','INSPECTION_REQUIRED','APPROVED','REJECTED','WITHDRAWN')),
  submitted_at timestamptz NULL,
  decided_at timestamptz NULL,
  decision_notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_certification_application_number UNIQUE (tenant_id, application_number)
);

CREATE TABLE compliance.certificate (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  certification_application_id uuid NOT NULL REFERENCES compliance.certification_application(id),
  certificate_number varchar(160) NOT NULL,
  standard_id uuid NOT NULL REFERENCES compliance.standard_catalog(id),
  compliance_profile_id uuid NOT NULL REFERENCES compliance.compliance_profile(id),
  issued_by varchar(240) NOT NULL,
  issued_at date NOT NULL,
  valid_from date NOT NULL,
  valid_until date NOT NULL,
  scope_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','SUSPENDED','REVOKED','EXPIRED','RENEWAL_DUE')),
  qr_token varchar(180) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_certificate_number UNIQUE (tenant_id, certificate_number),
  CONSTRAINT uq_certificate_qr UNIQUE (qr_token),
  CONSTRAINT ck_certificate_validity CHECK (valid_until >= valid_from)
);

CREATE TABLE compliance.inspection (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  compliance_profile_id uuid NOT NULL REFERENCES compliance.compliance_profile(id),
  certification_application_id uuid NULL REFERENCES compliance.certification_application(id),
  inspection_type varchar(60) NOT NULL
    CHECK (inspection_type IN ('INITIAL','SURVEILLANCE','RENEWAL','FOLLOW_UP','COMPLAINT','RANDOM','REGULATORY')),
  scheduled_at timestamptz NOT NULL,
  started_at timestamptz NULL,
  completed_at timestamptz NULL,
  inspector_name varchar(240) NULL,
  inspector_reference varchar(160) NULL,
  status varchar(30) NOT NULL DEFAULT 'SCHEDULED'
    CHECK (status IN ('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED','RESCHEDULED')),
  overall_result varchar(30) NULL
    CHECK (overall_result IS NULL OR overall_result IN ('PASS','PASS_WITH_OBSERVATIONS','FAIL','INCONCLUSIVE')),
  notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE compliance.inspection_finding (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  inspection_id uuid NOT NULL REFERENCES compliance.inspection(id) ON DELETE CASCADE,
  requirement_id uuid NULL REFERENCES compliance.requirement_catalog(id),
  finding_code varchar(120) NOT NULL,
  finding_type varchar(40) NOT NULL
    CHECK (finding_type IN ('CONFORMITY','OBSERVATION','MINOR_NONCONFORMITY','MAJOR_NONCONFORMITY','CRITICAL_NONCONFORMITY')),
  description text NOT NULL,
  evidence_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  corrective_action_required boolean NOT NULL DEFAULT false,
  due_date date NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','ACCEPTED','IN_PROGRESS','VERIFIED','CLOSED','REJECTED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE compliance.corrective_action (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  inspection_finding_id uuid NOT NULL REFERENCES compliance.inspection_finding(id),
  action_description text NOT NULL,
  owner_reference uuid NULL,
  due_date date NOT NULL,
  completed_at timestamptz NULL,
  verification_notes text NULL,
  verified_by uuid NULL,
  verified_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','IN_PROGRESS','COMPLETED','VERIFIED','REJECTED','OVERDUE')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_inspection_profile_status
  ON compliance.inspection(compliance_profile_id, status, scheduled_at DESC);

CREATE INDEX ix_inspection_finding_status
  ON compliance.inspection_finding(inspection_id, status, finding_type);

CREATE INDEX ix_corrective_action_due
  ON compliance.corrective_action(status, due_date);

CREATE TRIGGER trg_certification_application_updated_at
BEFORE UPDATE ON compliance.certification_application
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_inspection_updated_at
BEFORE UPDATE ON compliance.inspection
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
