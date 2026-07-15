CREATE TABLE crophealth.pest_catalog (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(100) NOT NULL,
  common_name varchar(200) NOT NULL,
  scientific_name varchar(240) NULL,
  pest_category varchar(80) NOT NULL,
  affected_crop_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  symptoms jsonb NOT NULL DEFAULT '[]'::jsonb,
  favorable_conditions jsonb NOT NULL DEFAULT '[]'::jsonb,
  lifecycle_notes text NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','IN_REVIEW','APPROVED','PUBLISHED','WITHDRAWN')),
  evidence_grade varchar(30) NOT NULL DEFAULT 'UNASSESSED',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_pest_catalog_code UNIQUE (tenant_id, code)
);

CREATE TABLE crophealth.disease_catalog (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  code varchar(100) NOT NULL,
  common_name varchar(200) NOT NULL,
  scientific_name varchar(240) NULL,
  disease_category varchar(80) NOT NULL,
  causal_agent varchar(240) NULL,
  affected_crop_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  symptoms jsonb NOT NULL DEFAULT '[]'::jsonb,
  favorable_conditions jsonb NOT NULL DEFAULT '[]'::jsonb,
  transmission_notes text NULL,
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','IN_REVIEW','APPROVED','PUBLISHED','WITHDRAWN')),
  evidence_grade varchar(30) NOT NULL DEFAULT 'UNASSESSED',
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_disease_catalog_code UNIQUE (tenant_id, code)
);

CREATE TABLE crophealth.scouting_visit (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  scouted_at timestamptz NOT NULL,
  scout_type varchar(40) NOT NULL
    CHECK (scout_type IN ('FARMER','FIELD_OFFICER','EXPERT','IOT','DRONE','SYSTEM')),
  scout_id uuid NULL,
  sampling_method varchar(100) NOT NULL,
  sample_area_hectares numeric(12,4) NULL CHECK (sample_area_hectares IS NULL OR sample_area_hectares > 0),
  plant_count integer NULL CHECK (plant_count IS NULL OR plant_count > 0),
  status varchar(30) NOT NULL DEFAULT 'COMPLETED'
    CHECK (status IN ('PLANNED','IN_PROGRESS','COMPLETED','CANCELLED')),
  notes text NULL,
  weather_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE crophealth.scouting_observation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  scouting_visit_id uuid NOT NULL REFERENCES crophealth.scouting_visit(id) ON DELETE CASCADE,
  observation_type varchar(40) NOT NULL
    CHECK (observation_type IN ('PEST','DISEASE','DEFICIENCY','WEED','ABIOTIC_STRESS','BENEFICIAL_INSECT','GENERAL')),
  pest_id uuid NULL REFERENCES crophealth.pest_catalog(id),
  disease_id uuid NULL REFERENCES crophealth.disease_catalog(id),
  observed_symptom varchar(300) NULL,
  affected_plant_count integer NULL CHECK (affected_plant_count IS NULL OR affected_plant_count >= 0),
  incidence_percent numeric(8,4) NULL
    CHECK (incidence_percent IS NULL OR (incidence_percent >= 0 AND incidence_percent <= 100)),
  severity_percent numeric(8,4) NULL
    CHECK (severity_percent IS NULL OR (severity_percent >= 0 AND severity_percent <= 100)),
  population_count numeric(18,4) NULL,
  population_unit varchar(60) NULL,
  crop_stage_code varchar(80) NULL,
  confidence_score numeric(8,4) NULL,
  evidence_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_observation_reference CHECK (
    (observation_type = 'PEST' AND pest_id IS NOT NULL)
    OR (observation_type = 'DISEASE' AND disease_id IS NOT NULL)
    OR (observation_type NOT IN ('PEST','DISEASE'))
  )
);

CREATE INDEX ix_scouting_visit_cycle_time
  ON crophealth.scouting_visit(crop_cycle_id, scouted_at DESC);

CREATE INDEX ix_scouting_observation_type
  ON crophealth.scouting_observation(observation_type, pest_id, disease_id);

CREATE TRIGGER trg_pest_catalog_updated_at
BEFORE UPDATE ON crophealth.pest_catalog
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();

CREATE TRIGGER trg_disease_catalog_updated_at
BEFORE UPDATE ON crophealth.disease_catalog
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
