CREATE TABLE crophealth.crop_health_assessment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  assessment_date date NOT NULL,
  health_status varchar(30) NOT NULL
    CHECK (health_status IN ('HEALTHY','WATCH','AT_RISK','AFFECTED','CRITICAL','RECOVERING')),
  vigor_score numeric(8,4) NULL,
  canopy_score numeric(8,4) NULL,
  stand_establishment_score numeric(8,4) NULL,
  pest_pressure_score numeric(8,4) NULL,
  disease_pressure_score numeric(8,4) NULL,
  nutrient_stress_score numeric(8,4) NULL,
  water_stress_score numeric(8,4) NULL,
  composite_score numeric(8,4) NOT NULL,
  confidence_score numeric(8,4) NOT NULL,
  reason_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  evidence_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  assessment_version varchar(60) NOT NULL DEFAULT 'v1',
  assessed_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_crop_health_assessment UNIQUE (crop_cycle_id, assessment_date)
);

CREATE TABLE crophealth.threshold_rule (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  pest_id uuid NULL REFERENCES crophealth.pest_catalog(id),
  disease_id uuid NULL REFERENCES crophealth.disease_catalog(id),
  threshold_type varchar(60) NOT NULL,
  lower_threshold numeric(18,6) NULL,
  upper_threshold numeric(18,6) NULL,
  unit_code varchar(60) NULL,
  crop_stage_code varchar(80) NULL,
  geography_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  action_level varchar(30) NOT NULL
    CHECK (action_level IN ('MONITOR','PREVENTIVE','INTERVENTION','EMERGENCY')),
  evidence_reference varchar(300) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','SUPERSEDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_threshold_subject CHECK (
    pest_id IS NOT NULL OR disease_id IS NOT NULL
  )
);

CREATE TABLE crophealth.outbreak_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  pest_id uuid NULL REFERENCES crophealth.pest_catalog(id),
  disease_id uuid NULL REFERENCES crophealth.disease_catalog(id),
  outbreak_type varchar(40) NOT NULL
    CHECK (outbreak_type IN ('PEST','DISEASE')),
  detected_at timestamptz NOT NULL,
  severity varchar(20) NOT NULL
    CHECK (severity IN ('LOW','MODERATE','HIGH','CRITICAL')),
  affected_area_hectares numeric(12,4) NULL,
  incidence_percent numeric(8,4) NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','CONTAINED','UNDER_TREATMENT','RESOLVED','CLOSED')),
  source_observation_id uuid NULL REFERENCES crophealth.scouting_observation(id),
  containment_notes text NULL,
  resolved_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_outbreak_subject CHECK (
    (outbreak_type = 'PEST' AND pest_id IS NOT NULL)
    OR
    (outbreak_type = 'DISEASE' AND disease_id IS NOT NULL)
  )
);

CREATE INDEX ix_crop_health_cycle_date
  ON crophealth.crop_health_assessment(crop_cycle_id, assessment_date DESC);

CREATE INDEX ix_threshold_rule_crop
  ON crophealth.threshold_rule(crop_id, pest_id, disease_id, status);

CREATE INDEX ix_outbreak_cycle_status
  ON crophealth.outbreak_event(crop_cycle_id, status, detected_at DESC);
