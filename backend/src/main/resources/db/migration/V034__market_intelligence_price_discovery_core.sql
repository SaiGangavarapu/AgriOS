CREATE SCHEMA IF NOT EXISTS market;

CREATE TABLE market.market_source (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_code varchar(100) NOT NULL,
  source_name varchar(240) NOT NULL,
  source_type varchar(60) NOT NULL
    CHECK (source_type IN ('GOVERNMENT','MANDI','MARKETPLACE','BUYER','WHOLESALE','RETAIL','COOPERATIVE','EXPORT')),
  base_url varchar(500) NULL,
  license_reference varchar(500) NULL,
  priority integer NOT NULL DEFAULT 100,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','DEGRADED','SUSPENDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_market_source_code UNIQUE (tenant_id, source_code)
);

CREATE TABLE market.market_location (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  location_code varchar(100) NOT NULL,
  location_name varchar(240) NOT NULL,
  location_type varchar(60) NOT NULL
    CHECK (location_type IN ('MANDI','WHOLESALE_MARKET','RETAIL_MARKET','PROCESSOR','EXPORT_HUB','COLLECTION_CENTER','ONLINE_MARKETPLACE')),
  district_code varchar(80) NULL,
  state_code varchar(80) NULL,
  country_code varchar(3) NOT NULL DEFAULT 'IND',
  latitude numeric(10,7) NULL,
  longitude numeric(10,7) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_market_location_code UNIQUE (tenant_id, location_code)
);

CREATE TABLE market.commodity_price_observation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_id uuid NOT NULL REFERENCES market.market_source(id),
  market_location_id uuid NULL REFERENCES market.market_location(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  grade_code varchar(40) NULL,
  observed_date date NOT NULL,
  min_price numeric(18,4) NULL,
  max_price numeric(18,4) NULL,
  modal_price numeric(18,4) NOT NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  quantity_unit varchar(40) NOT NULL,
  arrivals_quantity numeric(18,4) NULL,
  source_quality varchar(30) NOT NULL DEFAULT 'VALID'
    CHECK (source_quality IN ('VALID','SUSPECT','STALE','REJECTED')),
  raw_payload jsonb NOT NULL DEFAULT '{}'::jsonb,
  received_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_price_observation UNIQUE (
    source_id, market_location_id, crop_id, variety_id, grade_code, observed_date
  )
);

CREATE TABLE market.price_forecast (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  market_location_id uuid NULL REFERENCES market.market_location(id),
  forecast_date date NOT NULL,
  horizon_date date NOT NULL,
  forecast_price numeric(18,4) NOT NULL,
  lower_bound numeric(18,4) NULL,
  upper_bound numeric(18,4) NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  quantity_unit varchar(40) NOT NULL,
  confidence_score numeric(8,4) NOT NULL,
  model_name varchar(120) NOT NULL,
  model_version varchar(120) NOT NULL,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  feature_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','SUPERSEDED','EXPIRED','REJECTED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_price_forecast UNIQUE (
    tenant_id, crop_id, variety_id, market_location_id, forecast_date, horizon_date, model_version
  )
);

CREATE INDEX ix_price_observation_crop_date
  ON market.commodity_price_observation(crop_id, observed_date DESC);

CREATE INDEX ix_price_forecast_crop_horizon
  ON market.price_forecast(crop_id, horizon_date, status);

CREATE TRIGGER trg_market_source_updated_at
BEFORE UPDATE ON market.market_source
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
