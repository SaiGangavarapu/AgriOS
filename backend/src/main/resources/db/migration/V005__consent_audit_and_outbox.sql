CREATE TABLE consent.consent_grant (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  purpose_code varchar(80) NOT NULL,
  recipient_type varchar(60) NOT NULL,
  recipient_id varchar(120) NOT NULL,
  data_categories jsonb NOT NULL,
  scope_json jsonb NOT NULL,
  policy_version varchar(60) NOT NULL,
  language varchar(10) NOT NULL,
  valid_from timestamptz NOT NULL,
  valid_until timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','PRESENTED','ACTIVE','REVOKED','EXPIRED','REJECTED','SUPERSEDED')),
  granted_at timestamptz NULL,
  revoked_at timestamptz NULL,
  revoked_reason text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT ck_consent_validity CHECK (valid_until IS NULL OR valid_until > valid_from)
);

CREATE TABLE audit.audit_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL,
  programme_id uuid NULL,
  actor_id uuid NULL,
  actor_type varchar(30) NOT NULL,
  action varchar(120) NOT NULL,
  target_type varchar(120) NOT NULL,
  target_id uuid NULL,
  occurred_at timestamptz NOT NULL DEFAULT now(),
  correlation_id uuid NOT NULL,
  causation_id uuid NULL,
  reason text NULL,
  before_reference jsonb NULL,
  after_reference jsonb NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb
);

CREATE TABLE integration.outbox_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL,
  aggregate_type varchar(120) NOT NULL,
  aggregate_id uuid NOT NULL,
  aggregate_version bigint NOT NULL,
  event_type varchar(160) NOT NULL,
  event_version integer NOT NULL DEFAULT 1,
  payload jsonb NOT NULL,
  correlation_id uuid NOT NULL,
  causation_id uuid NULL,
  occurred_at timestamptz NOT NULL,
  published_at timestamptz NULL,
  publish_attempts integer NOT NULL DEFAULT 0,
  last_error text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE event_publication (
  id uuid PRIMARY KEY,
  publication_date timestamptz NOT NULL,
  listener_id varchar(255) NOT NULL,
  serialized_event varchar(255) NOT NULL,
  event_type varchar(255) NOT NULL,
  completion_date timestamptz NULL
);

CREATE TABLE event_publication_archive (
  id uuid PRIMARY KEY,
  publication_date timestamptz NOT NULL,
  listener_id varchar(255) NOT NULL,
  serialized_event varchar(255) NOT NULL,
  event_type varchar(255) NOT NULL,
  completion_date timestamptz NULL
);

CREATE TABLE integration.idempotency_record (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL,
  idempotency_key varchar(160) NOT NULL,
  operation_code varchar(120) NOT NULL,
  request_hash varchar(128) NOT NULL,
  response_status integer NULL,
  response_body jsonb NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  expires_at timestamptz NOT NULL,
  CONSTRAINT uq_idempotency UNIQUE (tenant_id, operation_code, idempotency_key)
);

CREATE TRIGGER trg_consent_updated_at
BEFORE UPDATE ON consent.consent_grant
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
