CREATE SCHEMA IF NOT EXISTS ai;

CREATE TABLE ai.ai_provider (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  provider_code varchar(100) NOT NULL,
  provider_name varchar(200) NOT NULL,
  provider_type varchar(60) NOT NULL
    CHECK (provider_type IN ('OPENAI','AZURE_OPENAI','ANTHROPIC','GOOGLE','OLLAMA','LOCAL','CUSTOM')),
  endpoint_reference varchar(500) NULL,
  credential_reference varchar(240) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','DEGRADED','SUSPENDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_ai_provider_code UNIQUE (tenant_id, provider_code)
);

CREATE TABLE ai.ai_model_profile (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  provider_id uuid NOT NULL REFERENCES ai.ai_provider(id),
  model_code varchar(120) NOT NULL,
  model_name varchar(200) NOT NULL,
  model_role varchar(60) NOT NULL
    CHECK (model_role IN ('CHAT','EMBEDDING','VISION','RERANKING','CLASSIFICATION','FORECASTING')),
  max_input_tokens integer NULL,
  max_output_tokens integer NULL,
  temperature numeric(6,4) NULL,
  enabled boolean NOT NULL DEFAULT true,
  default_for_role boolean NOT NULL DEFAULT false,
  configuration_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_ai_model_code UNIQUE (tenant_id, provider_id, model_code)
);

CREATE TABLE ai.prompt_template (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  template_code varchar(120) NOT NULL,
  template_name varchar(240) NOT NULL,
  assistant_type varchar(80) NOT NULL,
  language_code varchar(10) NOT NULL DEFAULT 'en',
  system_prompt text NOT NULL,
  user_prompt_template text NULL,
  version_no integer NOT NULL DEFAULT 1,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('DRAFT','ACTIVE','SUPERSEDED','INACTIVE')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_prompt_template UNIQUE (tenant_id, template_code, language_code, version_no)
);

CREATE TABLE ai.guardrail_policy (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  policy_code varchar(120) NOT NULL,
  policy_name varchar(240) NOT NULL,
  policy_type varchar(60) NOT NULL
    CHECK (policy_type IN ('INPUT','OUTPUT','TOOL','DATA_ACCESS','DOMAIN_SAFETY','COST','RATE_LIMIT')),
  policy_definition jsonb NOT NULL,
  priority integer NOT NULL DEFAULT 100,
  enabled boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_guardrail_policy_code UNIQUE (tenant_id, policy_code)
);

CREATE INDEX ix_ai_model_role
  ON ai.ai_model_profile(tenant_id, model_role, enabled, default_for_role);

CREATE INDEX ix_prompt_template_lookup
  ON ai.prompt_template(tenant_id, assistant_type, language_code, status, version_no DESC);

CREATE TRIGGER trg_ai_provider_updated_at
BEFORE UPDATE ON ai.ai_provider
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
