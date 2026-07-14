CREATE TABLE weather.monsoon_season (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  region_code varchar(80) NOT NULL,
  monsoon_type varchar(40) NOT NULL
    CHECK (monsoon_type IN ('SOUTHWEST','NORTHEAST','LOCAL_RAINY_SEASON')),
  season_year integer NOT NULL,
  climatology_onset_date date NULL,
  climatology_withdrawal_date date NULL,
  predicted_onset_date date NULL,
  predicted_withdrawal_date date NULL,
  actual_onset_date date NULL,
  actual_withdrawal_date date NULL,
  onset_confidence numeric(8,4) NULL,
  withdrawal_confidence numeric(8,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'MONITORING'
    CHECK (status IN ('MONITORING','ONSET_PREDICTED','ONSET_CONFIRMED','ACTIVE','WITHDRAWAL_PREDICTED','WITHDRAWN','CLOSED')),
  basis_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_monsoon_region_year_type UNIQUE (tenant_id, region_code, season_year, monsoon_type)
);

CREATE TABLE weather.monsoon_indicator (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  monsoon_season_id uuid NOT NULL REFERENCES weather.monsoon_season(id) ON DELETE CASCADE,
  indicator_code varchar(80) NOT NULL,
  indicator_value numeric(18,6) NULL,
  indicator_text varchar(300) NULL,
  unit_code varchar(40) NULL,
  observed_at timestamptz NOT NULL,
  source_reference varchar(200) NULL,
  confidence_score numeric(8,4) NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE weather.climate_risk_assessment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  assessment_window_start date NOT NULL,
  assessment_window_end date NOT NULL,
  drought_risk varchar(20) NOT NULL,
  flood_risk varchar(20) NOT NULL,
  heat_risk varchar(20) NOT NULL,
  cold_risk varchar(20) NOT NULL,
  wind_risk varchar(20) NOT NULL,
  excess_rain_risk varchar(20) NOT NULL,
  composite_score numeric(8,4) NOT NULL,
  confidence_score numeric(8,4) NOT NULL,
  source_versions jsonb NOT NULL DEFAULT '{}'::jsonb,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  assessed_at timestamptz NOT NULL DEFAULT now(),
  assessment_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT ck_climate_risk_window CHECK (assessment_window_end >= assessment_window_start)
);

CREATE TABLE weather.field_weather_summary (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  summary_date date NOT NULL,
  rainfall_total_mm numeric(12,4) NOT NULL DEFAULT 0,
  temperature_min_c numeric(8,3) NULL,
  temperature_max_c numeric(8,3) NULL,
  humidity_avg_percent numeric(8,3) NULL,
  evapotranspiration_mm numeric(12,4) NULL,
  growing_degree_days numeric(12,4) NULL,
  dry_spell_days integer NOT NULL DEFAULT 0,
  data_quality varchar(30) NOT NULL DEFAULT 'VALID',
  calculated_at timestamptz NOT NULL DEFAULT now(),
  calculation_version varchar(60) NOT NULL DEFAULT 'v1',
  CONSTRAINT uq_field_weather_summary UNIQUE (field_id, summary_date)
);

CREATE INDEX ix_monsoon_region_year
  ON weather.monsoon_season(region_code, season_year DESC);

CREATE INDEX ix_climate_risk_field_window
  ON weather.climate_risk_assessment(field_id, assessment_window_start, assessment_window_end);

CREATE INDEX ix_weather_summary_field_date
  ON weather.field_weather_summary(field_id, summary_date DESC);

CREATE TRIGGER trg_monsoon_season_updated_at
BEFORE UPDATE ON weather.monsoon_season
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
