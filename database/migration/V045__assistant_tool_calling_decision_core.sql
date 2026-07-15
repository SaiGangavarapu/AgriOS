CREATE TABLE ai.assistant_session (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  assistant_type varchar(80) NOT NULL
    CHECK (assistant_type IN ('GENERAL','CROP_PLANNING','IRRIGATION','NUTRIENT','PEST_DISEASE','WEATHER','MARKET','FINANCE','COMPLIANCE','ORGANIZATION')),
  title varchar(300) NULL,
  language_code varchar(10) NOT NULL DEFAULT 'en',
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','CLOSED','ARCHIVED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE ai.assistant_message (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  assistant_session_id uuid NOT NULL REFERENCES ai.assistant_session(id) ON DELETE CASCADE,
  message_role varchar(20) NOT NULL
    CHECK (message_role IN ('SYSTEM','USER','ASSISTANT','TOOL')),
  message_text text NOT NULL,
  model_code varchar(120) NULL,
  prompt_tokens integer NULL,
  completion_tokens integer NULL,
  latency_ms bigint NULL,
  finish_reason varchar(80) NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE ai.tool_definition (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  tool_code varchar(120) NOT NULL,
  tool_name varchar(240) NOT NULL,
  domain_module varchar(100) NOT NULL,
  description text NOT NULL,
  input_schema jsonb NOT NULL,
  output_schema jsonb NOT NULL DEFAULT '{}'::jsonb,
  risk_level varchar(20) NOT NULL DEFAULT 'LOW'
    CHECK (risk_level IN ('LOW','MEDIUM','HIGH','CRITICAL')),
  requires_confirmation boolean NOT NULL DEFAULT false,
  enabled boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_tool_definition_code UNIQUE (tenant_id, tool_code)
);

CREATE TABLE ai.tool_execution (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  assistant_session_id uuid NULL REFERENCES ai.assistant_session(id),
  assistant_message_id uuid NULL REFERENCES ai.assistant_message(id),
  tool_definition_id uuid NOT NULL REFERENCES ai.tool_definition(id),
  idempotency_key varchar(180) NOT NULL,
  input_payload jsonb NOT NULL,
  output_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'REQUESTED'
    CHECK (status IN ('REQUESTED','AWAITING_CONFIRMATION','APPROVED','RUNNING','SUCCEEDED','FAILED','REJECTED','CANCELLED')),
  error_code varchar(120) NULL,
  error_message text NULL,
  requested_at timestamptz NOT NULL DEFAULT now(),
  completed_at timestamptz NULL,
  CONSTRAINT uq_tool_execution_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE ai.decision_record (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  decision_type varchar(100) NOT NULL,
  recommendation text NOT NULL,
  confidence_score numeric(8,4) NULL,
  risk_level varchar(20) NOT NULL DEFAULT 'LOW',
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  evidence_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  model_code varchar(120) NULL,
  prompt_template_code varchar(120) NULL,
  human_review_status varchar(30) NOT NULL DEFAULT 'NOT_REQUIRED'
    CHECK (human_review_status IN ('NOT_REQUIRED','PENDING','APPROVED','REJECTED','OVERRIDDEN')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE ai.response_citation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  assistant_message_id uuid NOT NULL REFERENCES ai.assistant_message(id) ON DELETE CASCADE,
  knowledge_chunk_id uuid NULL REFERENCES ai.knowledge_chunk(id),
  source_label varchar(240) NOT NULL,
  source_reference varchar(500) NULL,
  quoted_text text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_assistant_session_farmer
  ON ai.assistant_session(tenant_id, farmer_id, status, updated_at DESC);

CREATE INDEX ix_assistant_message_session
  ON ai.assistant_message(assistant_session_id, created_at);

CREATE INDEX ix_tool_execution_status
  ON ai.tool_execution(tenant_id, status, requested_at DESC);

CREATE INDEX ix_decision_record_scope
  ON ai.decision_record(tenant_id, farmer_id, farm_id, field_id, crop_cycle_id, created_at DESC);

CREATE TRIGGER trg_assistant_session_updated_at
BEFORE UPDATE ON ai.assistant_session
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
