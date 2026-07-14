CREATE TABLE irrigation.automation_policy (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  irrigation_plan_id uuid NOT NULL REFERENCES irrigation.irrigation_plan(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  policy_name varchar(200) NOT NULL,
  control_mode varchar(40) NOT NULL
    CHECK (control_mode IN ('ADVISORY_ONLY','APPROVAL_REQUIRED','SEMI_AUTOMATIC','FULL_AUTOMATIC')),
  moisture_stream_id uuid NULL REFERENCES telemetry.telemetry_stream(id),
  rain_stream_id uuid NULL REFERENCES telemetry.telemetry_stream(id),
  flow_stream_id uuid NULL REFERENCES telemetry.telemetry_stream(id),
  actuator_device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  moisture_trigger_below numeric(18,6) NULL,
  rain_skip_threshold_mm numeric(18,6) NULL,
  maximum_runtime_minutes integer NOT NULL CHECK (maximum_runtime_minutes > 0),
  minimum_interval_minutes integer NOT NULL DEFAULT 60 CHECK (minimum_interval_minutes >= 0),
  allowed_start_time time NULL,
  allowed_end_time time NULL,
  emergency_stop_enabled boolean NOT NULL DEFAULT true,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','APPROVED','ACTIVE','PAUSED','SUSPENDED','RETIRED')),
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE irrigation.automation_decision (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  automation_policy_id uuid NOT NULL REFERENCES irrigation.automation_policy(id),
  irrigation_schedule_id uuid NULL REFERENCES irrigation.irrigation_schedule(id),
  decided_at timestamptz NOT NULL,
  decision_type varchar(40) NOT NULL
    CHECK (decision_type IN ('START','SKIP','DEFER','STOP','NO_ACTION','REQUIRES_APPROVAL')),
  decision_status varchar(30) NOT NULL DEFAULT 'PENDING'
    CHECK (decision_status IN ('PENDING','APPROVED','REJECTED','EXECUTED','EXPIRED','FAILED')),
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  input_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  confidence_score numeric(8,4) NOT NULL,
  requested_by varchar(60) NOT NULL DEFAULT 'RULE_ENGINE',
  approved_by uuid NULL,
  approved_at timestamptz NULL,
  executed_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE irrigation.actuator_command (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  automation_decision_id uuid NOT NULL REFERENCES irrigation.automation_decision(id),
  actuator_device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  command_type varchar(40) NOT NULL
    CHECK (command_type IN ('OPEN_VALVE','CLOSE_VALVE','START_PUMP','STOP_PUMP','SET_FLOW_RATE','EMERGENCY_STOP')),
  command_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  issued_at timestamptz NOT NULL,
  acknowledged_at timestamptz NULL,
  completed_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'QUEUED'
    CHECK (status IN ('QUEUED','SENT','ACKNOWLEDGED','COMPLETED','FAILED','TIMED_OUT','CANCELLED')),
  failure_reason text NULL,
  idempotency_key varchar(160) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_actuator_command_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE irrigation.automation_safety_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  automation_policy_id uuid NOT NULL REFERENCES irrigation.automation_policy(id),
  actuator_device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  event_type varchar(80) NOT NULL,
  severity varchar(20) NOT NULL,
  occurred_at timestamptz NOT NULL,
  description text NOT NULL,
  context jsonb NOT NULL DEFAULT '{}'::jsonb,
  resolved_at timestamptz NULL,
  resolved_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_automation_policy_cycle_status
  ON irrigation.automation_policy(crop_cycle_id, status);

CREATE INDEX ix_automation_decision_policy_time
  ON irrigation.automation_decision(automation_policy_id, decided_at DESC);

CREATE INDEX ix_actuator_command_status
  ON irrigation.actuator_command(actuator_device_id, status, issued_at);

CREATE INDEX ix_automation_safety_open
  ON irrigation.automation_safety_event(automation_policy_id, occurred_at DESC)
  WHERE resolved_at IS NULL;

CREATE TRIGGER trg_automation_policy_updated_at
BEFORE UPDATE ON irrigation.automation_policy
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
