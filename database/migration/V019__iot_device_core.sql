CREATE TABLE iotdevice.device (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  device_uid varchar(160) NOT NULL,
  device_type varchar(80) NOT NULL,
  manufacturer varchar(160) NULL,
  model varchar(160) NULL,
  firmware_version varchar(120) NULL,
  communication_protocol varchar(60) NOT NULL,
  authentication_mode varchar(60) NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'REGISTERED'
    CHECK (status IN ('REGISTERED','PROVISIONED','ACTIVE','INACTIVE','MAINTENANCE','SUSPENDED','RETIRED','COMPROMISED')),
  last_seen_at timestamptz NULL,
  commissioned_at timestamptz NULL,
  retired_at timestamptz NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_device_tenant_uid UNIQUE (tenant_id, device_uid)
);

CREATE TABLE iotdevice.device_assignment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  assignment_type varchar(50) NOT NULL
    CHECK (assignment_type IN ('FIELD','WATER_SOURCE','FARM','WEATHER_LOCATION','IRRIGATION_PLAN')),
  assigned_entity_id uuid NOT NULL,
  valid_from timestamptz NOT NULL DEFAULT now(),
  valid_until timestamptz NULL,
  is_current boolean NOT NULL DEFAULT true,
  notes text NULL,
  assigned_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_device_assignment_validity CHECK (valid_until IS NULL OR valid_until > valid_from)
);

CREATE UNIQUE INDEX uq_current_device_assignment
  ON iotdevice.device_assignment(device_id)
  WHERE is_current;

CREATE TABLE iotdevice.device_credential (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  credential_type varchar(60) NOT NULL,
  credential_reference varchar(255) NOT NULL,
  issued_at timestamptz NOT NULL,
  expires_at timestamptz NULL,
  revoked_at timestamptz NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','EXPIRED','REVOKED','ROTATED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE iotdevice.device_health (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id uuid NOT NULL REFERENCES iotdevice.device(id),
  recorded_at timestamptz NOT NULL,
  battery_percent numeric(8,3) NULL,
  signal_strength_dbm numeric(10,3) NULL,
  internal_temperature_c numeric(8,3) NULL,
  uptime_seconds bigint NULL,
  storage_free_bytes bigint NULL,
  error_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  health_status varchar(30) NOT NULL DEFAULT 'HEALTHY'
    CHECK (health_status IN ('HEALTHY','DEGRADED','CRITICAL','OFFLINE','UNKNOWN')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_device_status
  ON iotdevice.device(tenant_id, status, device_type);

CREATE INDEX ix_device_assignment_entity
  ON iotdevice.device_assignment(assignment_type, assigned_entity_id, is_current);

CREATE INDEX ix_device_health_recent
  ON iotdevice.device_health(device_id, recorded_at DESC);

CREATE TRIGGER trg_device_updated_at
BEFORE UPDATE ON iotdevice.device
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
