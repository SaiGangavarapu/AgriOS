CREATE SCHEMA IF NOT EXISTS notification;

CREATE TABLE notification.notification_template (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  template_code varchar(100) NOT NULL,
  channel varchar(30) NOT NULL
    CHECK (channel IN ('IN_APP','SMS','WHATSAPP','EMAIL','VOICE')),
  language varchar(10) NOT NULL,
  subject_template varchar(300) NULL,
  body_template text NOT NULL,
  version_no integer NOT NULL DEFAULT 1,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','INACTIVE','SUPERSEDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_notification_template UNIQUE (tenant_id, template_code, channel, language, version_no)
);

CREATE TABLE notification.notification (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  recipient_user_id uuid NULL REFERENCES identity.user_account(id),
  advisory_id uuid NULL REFERENCES advisory.advisory(id),
  review_case_id uuid NULL REFERENCES advisory.expert_review_case(id),
  notification_type varchar(80) NOT NULL,
  channel varchar(30) NOT NULL
    CHECK (channel IN ('IN_APP','SMS','WHATSAPP','EMAIL','VOICE')),
  language varchar(10) NOT NULL,
  recipient_address varchar(300) NULL,
  subject varchar(300) NULL,
  body text NOT NULL,
  priority varchar(20) NOT NULL DEFAULT 'NORMAL',
  scheduled_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'QUEUED'
    CHECK (status IN ('QUEUED','SCHEDULED','SENDING','SENT','DELIVERED','FAILED','CANCELLED','EXPIRED','READ')),
  provider_reference varchar(240) NULL,
  attempt_count integer NOT NULL DEFAULT 0,
  last_attempt_at timestamptz NULL,
  sent_at timestamptz NULL,
  delivered_at timestamptz NULL,
  read_at timestamptz NULL,
  failure_code varchar(120) NULL,
  failure_message text NULL,
  idempotency_key varchar(160) NOT NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_notification_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE notification.notification_attempt (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  notification_id uuid NOT NULL REFERENCES notification.notification(id) ON DELETE CASCADE,
  attempt_no integer NOT NULL,
  attempted_at timestamptz NOT NULL DEFAULT now(),
  provider_name varchar(160) NULL,
  request_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  response_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  result_status varchar(30) NOT NULL
    CHECK (result_status IN ('SENT','DELIVERED','FAILED','RETRY_SCHEDULED')),
  error_code varchar(120) NULL,
  error_message text NULL,
  next_retry_at timestamptz NULL,
  CONSTRAINT uq_notification_attempt UNIQUE (notification_id, attempt_no)
);

CREATE TABLE notification.notification_preference (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  notification_type varchar(80) NOT NULL,
  channel varchar(30) NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  quiet_hours_start time NULL,
  quiet_hours_end time NULL,
  preferred_language varchar(10) NOT NULL DEFAULT 'en',
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_notification_preference UNIQUE (tenant_id, farmer_id, notification_type, channel)
);

CREATE INDEX ix_notification_queue
  ON notification.notification(status, scheduled_at, priority, created_at);

CREATE INDEX ix_notification_farmer
  ON notification.notification(farmer_id, status, created_at DESC);

CREATE TRIGGER trg_notification_preference_updated_at
BEFORE UPDATE ON notification.notification_preference
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
