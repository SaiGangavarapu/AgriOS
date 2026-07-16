CREATE SCHEMA IF NOT EXISTS weather;

CREATE TABLE weather.weather_source (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(80) NOT NULL,
  name varchar(200) NOT NULL,
  source_type varchar(50) NOT NULL,
  provider_name varchar(200) NOT NULL,
  base_url varchar(500) NULL,
  license_reference varchar(500) NULL,
  priority integer NOT NULL DEFAULT 100,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','DEGRADED','SUSPENDED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_weather_source_tenant_code UNIQUE (tenant_id, code)
);

CREATE TABLE weather.weather_location (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NULL REFERENCES farm.field(id),
  location_type varchar(40) NOT NULL,
  latitude numeric(10,7) NOT NULL,
  longitude numeric(10,7) NOT NULL,
  elevation_meters numeric(10,2) NULL,
  grid_reference varchar(120) NULL,
  timezone varchar(80) NOT NULL DEFAULT 'Asia/Kolkata',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_weather_location_field UNIQUE (field_id)
);

CREATE TABLE weather.observation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_id uuid NOT NULL REFERENCES weather.weather_source(id),
  location_id uuid NOT NULL REFERENCES weather.weather_location(id),
  observed_at timestamptz NOT NULL,
  temperature_c numeric(8,3) NULL,
  relative_humidity_percent numeric(8,3) NULL,
  rainfall_mm numeric(12,4) NULL,
  wind_speed_kph numeric(10,3) NULL,
  wind_direction_degrees numeric(8,3) NULL,
  solar_radiation_wm2 numeric(12,4) NULL,
  pressure_hpa numeric(10,3) NULL,
  soil_temperature_c numeric(8,3) NULL,
  source_quality varchar(30) NOT NULL DEFAULT 'VALID',
  raw_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  received_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_weather_observation UNIQUE (source_id, location_id, observed_at)
);

CREATE TABLE weather.forecast_run (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_id uuid NOT NULL REFERENCES weather.weather_source(id),
  location_id uuid NOT NULL REFERENCES weather.weather_location(id),
  issued_at timestamptz NOT NULL,
  valid_from timestamptz NOT NULL,
  valid_until timestamptz NOT NULL,
  model_name varchar(120) NULL,
  model_version varchar(120) NULL,
  confidence_score numeric(8,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'VALID'
    CHECK (status IN ('VALID','SUPERSEDED','REJECTED','EXPIRED')),
  received_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_forecast_validity CHECK (valid_until > valid_from)
);

CREATE TABLE weather.forecast_period (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  forecast_run_id uuid NOT NULL REFERENCES weather.forecast_run(id) ON DELETE CASCADE,
  period_start timestamptz NOT NULL,
  period_end timestamptz NOT NULL,
  temperature_min_c numeric(8,3) NULL,
  temperature_max_c numeric(8,3) NULL,
  rainfall_mm numeric(12,4) NULL,
  precipitation_probability numeric(8,4) NULL,
  relative_humidity_percent numeric(8,3) NULL,
  wind_speed_kph numeric(10,3) NULL,
  wind_gust_kph numeric(10,3) NULL,
  weather_code varchar(80) NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_forecast_period CHECK (period_end > period_start)
);

CREATE TABLE weather.warning (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_id uuid NOT NULL REFERENCES weather.weather_source(id),
  location_id uuid NOT NULL REFERENCES weather.weather_location(id),
  external_warning_id varchar(160) NULL,
  warning_type varchar(80) NOT NULL,
  severity varchar(20) NOT NULL
    CHECK (severity IN ('INFO','WATCH','ADVISORY','WARNING','EMERGENCY')),
  headline varchar(300) NOT NULL,
  description text NULL,
  valid_from timestamptz NOT NULL,
  valid_until timestamptz NULL,
  issued_at timestamptz NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','EXPIRED','CANCELLED','SUPERSEDED')),
  raw_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_weather_observation_location_time
  ON weather.observation(location_id, observed_at DESC);

CREATE INDEX ix_forecast_run_location_issue
  ON weather.forecast_run(location_id, issued_at DESC);

CREATE INDEX ix_forecast_period_time
  ON weather.forecast_period(forecast_run_id, period_start);

CREATE INDEX ix_warning_location_status
  ON weather.warning(location_id, status, valid_from);

CREATE TRIGGER trg_weather_source_updated_at
BEFORE UPDATE ON weather.weather_source
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
