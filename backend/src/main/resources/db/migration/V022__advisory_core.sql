CREATE SCHEMA IF NOT EXISTS advisory;

CREATE TABLE advisory.advisory (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  advisory_type varchar(80) NOT NULL,
  priority varchar(20) NOT NULL DEFAULT 'NORMAL'
    CHECK (priority IN ('CRITICAL','HIGH','NORMAL','LOW')),
  title varchar(300) NOT NULL,
  summary text NOT NULL,
  detailed_guidance text NULL,
  source_type varchar(60) NOT NULL
    CHECK (source_type IN ('RULE_ENGINE','EXPERT','WEATHER','IOT','SOIL_WATER','CROP_PLAN','SYSTEM')),
  source_reference_id uuid NULL,
  language varchar(10) NOT NULL DEFAULT 'en',
  valid_from timestamptz NOT NULL DEFAULT now(),
  valid_until timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','PENDING_REVIEW','APPROVED','PUBLISHED','WITHDRAWN','EXPIRED','REJECTED')),
  confidence_score numeric(8,4) NULL,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  evidence_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  published_at timestamptz NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_advisory_validity CHECK (valid_until IS NULL OR valid_until > valid_from)
);

CREATE TABLE advisory.advisory_action (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  advisory_id uuid NOT NULL REFERENCES advisory.advisory(id) ON DELETE CASCADE,
  action_code varchar(80) NOT NULL,
  action_label varchar(240) NOT NULL,
  action_description text NULL,
  due_at timestamptz NULL,
  action_priority varchar(20) NOT NULL DEFAULT 'NORMAL'
    CHECK (action_priority IN ('CRITICAL','HIGH','NORMAL','LOW')),
  task_template_type varchar(80) NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','ACKNOWLEDGED','COMPLETED','DISMISSED','EXPIRED')),
  completion_reference_type varchar(80) NULL,
  completion_reference_id uuid NULL,
  completed_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE advisory.advisory_feedback (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  advisory_id uuid NOT NULL REFERENCES advisory.advisory(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  feedback_type varchar(40) NOT NULL
    CHECK (feedback_type IN ('HELPFUL','NOT_HELPFUL','NOT_UNDERSTOOD','NOT_APPLICABLE','FOLLOWED','NOT_FOLLOWED')),
  rating integer NULL CHECK (rating IS NULL OR (rating >= 1 AND rating <= 5)),
  comments text NULL,
  submitted_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_advisory_target_status
  ON advisory.advisory(tenant_id, farmer_id, field_id, crop_cycle_id, status, valid_from DESC);

CREATE INDEX ix_advisory_priority_status
  ON advisory.advisory(tenant_id, priority, status, valid_from DESC);

CREATE INDEX ix_advisory_action_due
  ON advisory.advisory_action(status, due_at);

CREATE TRIGGER trg_advisory_updated_at
BEFORE UPDATE ON advisory.advisory
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
