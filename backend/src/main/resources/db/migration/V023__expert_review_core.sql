CREATE TABLE advisory.expert_profile (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  user_account_id uuid NULL REFERENCES identity.user_account(id),
  display_name varchar(200) NOT NULL,
  expert_type varchar(80) NOT NULL,
  organization_name varchar(240) NULL,
  qualification_summary text NULL,
  registration_reference varchar(160) NULL,
  geography_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  crop_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  language_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED','RETIRED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE advisory.expert_review_case (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  case_type varchar(80) NOT NULL,
  subject_type varchar(80) NOT NULL,
  subject_id uuid NOT NULL,
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  requested_by uuid NULL,
  requested_at timestamptz NOT NULL DEFAULT now(),
  priority varchar(20) NOT NULL DEFAULT 'NORMAL'
    CHECK (priority IN ('CRITICAL','HIGH','NORMAL','LOW')),
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','ASSIGNED','IN_REVIEW','MORE_INFORMATION_REQUIRED','COMPLETED','REJECTED','CANCELLED')),
  due_at timestamptz NULL,
  question text NOT NULL,
  context_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  assigned_expert_id uuid NULL REFERENCES advisory.expert_profile(id),
  assigned_at timestamptz NULL,
  completed_at timestamptz NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE advisory.expert_review_note (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  review_case_id uuid NOT NULL REFERENCES advisory.expert_review_case(id) ON DELETE CASCADE,
  expert_id uuid NOT NULL REFERENCES advisory.expert_profile(id),
  note_type varchar(40) NOT NULL
    CHECK (note_type IN ('INTERNAL','FARMER_VISIBLE','RECOMMENDATION','REQUEST_INFORMATION','DECISION')),
  note_text text NOT NULL,
  evidence_references jsonb NOT NULL DEFAULT '[]'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE advisory.expert_review_decision (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  review_case_id uuid NOT NULL UNIQUE REFERENCES advisory.expert_review_case(id),
  expert_id uuid NOT NULL REFERENCES advisory.expert_profile(id),
  decision_code varchar(80) NOT NULL,
  decision_summary text NOT NULL,
  recommendation_text text NULL,
  risk_level varchar(20) NULL,
  follow_up_required boolean NOT NULL DEFAULT false,
  follow_up_due_at timestamptz NULL,
  advisory_id uuid NULL REFERENCES advisory.advisory(id),
  decided_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_expert_review_case_status
  ON advisory.expert_review_case(tenant_id, status, priority, requested_at);

CREATE INDEX ix_expert_review_case_expert
  ON advisory.expert_review_case(assigned_expert_id, status, due_at);

CREATE TRIGGER trg_expert_profile_updated_at
BEFORE UPDATE ON advisory.expert_profile
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_expert_review_case_updated_at
BEFORE UPDATE ON advisory.expert_review_case
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
