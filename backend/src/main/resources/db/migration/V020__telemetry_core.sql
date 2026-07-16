CREATE SCHEMA IF NOT EXISTS telemetry;

CREATE TABLE telemetry.telemetry_stream (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  stream_code varchar(100) NOT NULL,
  measurement_type varchar(80) NOT NULL,
  unit_code varchar(40) NOT NULL,
  sampling_interval_seconds integer NULL,
  expected_min numeric(18,6) NULL,
  expected_max numeric(18,6) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','PAUSED','INACTIVE','RETIRED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_telemetry_stream UNIQUE (device_id, stream_code)
);

CREATE TABLE telemetry.telemetry_reading (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  stream_id uuid NOT NULL REFERENCES telemetry.telemetry_stream(id),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  observed_at timestamptz NOT NULL,
  received_at timestamptz NOT NULL DEFAULT now(),
  numeric_value numeric(18,6) NULL,
  text_value varchar(500) NULL,
  quality_flag varchar(30) NOT NULL DEFAULT 'VALID'
    CHECK (quality_flag IN ('VALID','SUSPECT','OUT_OF_RANGE','MISSING','DUPLICATE','LATE','REJECTED')),
  sequence_no bigint NULL,
  raw_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  CONSTRAINT ck_telemetry_value CHECK (numeric_value IS NOT NULL OR text_value IS NOT NULL),
  CONSTRAINT uq_telemetry_reading UNIQUE (stream_id, observed_at, sequence_no)
);

CREATE TABLE telemetry.telemetry_aggregate (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  stream_id uuid NOT NULL REFERENCES telemetry.telemetry_stream(id),
  bucket_start timestamptz NOT NULL,
  bucket_end timestamptz NOT NULL,
  sample_count integer NOT NULL,
  min_value numeric(18,6) NULL,
  max_value numeric(18,6) NULL,
  avg_value numeric(18,6) NULL,
  sum_value numeric(18,6) NULL,
  quality_summary jsonb NOT NULL DEFAULT '{}'::jsonb,
  calculated_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT uq_telemetry_aggregate UNIQUE (stream_id, bucket_start, bucket_end),
  CONSTRAINT ck_telemetry_bucket CHECK (bucket_end > bucket_start)
);

CREATE TABLE telemetry.telemetry_alert (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  stream_id uuid NULL REFERENCES telemetry.telemetry_stream(id),
  alert_type varchar(80) NOT NULL,
  severity varchar(20) NOT NULL
    CHECK (severity IN ('INFO','WARNING','CRITICAL','EMERGENCY')),
  triggered_at timestamptz NOT NULL,
  cleared_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','ACKNOWLEDGED','CLEARED','SUPPRESSED')),
  message varchar(500) NOT NULL,
  context jsonb NOT NULL DEFAULT '{}'::jsonb,
  acknowledged_by uuid NULL,
  acknowledged_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_telemetry_reading_stream_time
  ON telemetry.telemetry_reading(stream_id, observed_at DESC);

CREATE INDEX ix_telemetry_reading_device_time
  ON telemetry.telemetry_reading(device_id, observed_at DESC);

CREATE INDEX ix_telemetry_alert_open
  ON telemetry.telemetry_alert(tenant_id, status, severity, triggered_at DESC);
