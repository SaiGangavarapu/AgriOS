CREATE TABLE crophealth.ipm_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  pest_id uuid NULL REFERENCES crophealth.pest_catalog(id),
  disease_id uuid NULL REFERENCES crophealth.disease_catalog(id),
  plan_type varchar(40) NOT NULL
    CHECK (plan_type IN ('PEST','DISEASE','GENERAL_IPM')),
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','GENERATED','EXPERT_REVIEW','APPROVED','ACTIVE','COMPLETED','CANCELLED','SUPERSEDED')),
  strategy_summary text NOT NULL,
  prevention_measures jsonb NOT NULL DEFAULT '[]'::jsonb,
  monitoring_measures jsonb NOT NULL DEFAULT '[]'::jsonb,
  non_chemical_measures jsonb NOT NULL DEFAULT '[]'::jsonb,
  chemical_last_resort boolean NOT NULL DEFAULT true,
  warnings jsonb NOT NULL DEFAULT '[]'::jsonb,
  approval_notes text NULL,
  approved_at timestamptz NULL,
  approved_by uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_ipm_plan_subject CHECK (
    plan_type = 'GENERAL_IPM'
    OR (plan_type = 'PEST' AND pest_id IS NOT NULL)
    OR (plan_type = 'DISEASE' AND disease_id IS NOT NULL)
  )
);

CREATE TABLE crophealth.ipm_action (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  ipm_plan_id uuid NOT NULL REFERENCES crophealth.ipm_plan(id) ON DELETE CASCADE,
  action_sequence integer NOT NULL,
  action_category varchar(60) NOT NULL
    CHECK (action_category IN ('CULTURAL','MECHANICAL','PHYSICAL','BIOLOGICAL','BOTANICAL','BEHAVIORAL','CHEMICAL','MONITORING','SANITATION')),
  action_name varchar(240) NOT NULL,
  action_description text NULL,
  trigger_condition varchar(300) NULL,
  crop_stage_code varchar(80) NULL,
  planned_date date NULL,
  product_name varchar(200) NULL,
  dosage numeric(18,4) NULL,
  dosage_unit varchar(60) NULL,
  application_method varchar(100) NULL,
  pre_harvest_interval_days integer NULL,
  reentry_interval_hours integer NULL,
  pollinator_warning boolean NOT NULL DEFAULT false,
  water_body_buffer_meters numeric(10,2) NULL,
  status varchar(30) NOT NULL DEFAULT 'PLANNED'
    CHECK (status IN ('PLANNED','DUE','IN_PROGRESS','COMPLETED','SKIPPED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_ipm_action_sequence UNIQUE (ipm_plan_id, action_sequence)
);

CREATE TABLE crophealth.ipm_execution (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  ipm_plan_id uuid NOT NULL REFERENCES crophealth.ipm_plan(id),
  ipm_action_id uuid NOT NULL REFERENCES crophealth.ipm_action(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  executed_at timestamptz NOT NULL,
  executed_quantity numeric(18,4) NULL,
  quantity_unit varchar(60) NULL,
  covered_area_hectares numeric(12,4) NULL,
  weather_check_status varchar(30) NOT NULL DEFAULT 'NOT_CHECKED',
  safety_validation_status varchar(30) NOT NULL DEFAULT 'PENDING',
  observed_result varchar(300) NULL,
  notes text NULL,
  executed_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE crophealth.ipm_effectiveness_review (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  ipm_plan_id uuid NOT NULL REFERENCES crophealth.ipm_plan(id),
  review_date date NOT NULL,
  pre_treatment_incidence numeric(8,4) NULL,
  post_treatment_incidence numeric(8,4) NULL,
  effectiveness_percent numeric(8,4) NULL,
  crop_damage_reduced boolean NULL,
  beneficial_insect_impact varchar(30) NULL,
  resistance_concern boolean NOT NULL DEFAULT false,
  recommendation text NULL,
  reviewed_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_ipm_plan_cycle_status
  ON crophealth.ipm_plan(crop_cycle_id, status, created_at DESC);

CREATE INDEX ix_ipm_action_due
  ON crophealth.ipm_action(ipm_plan_id, status, planned_date);

CREATE INDEX ix_ipm_execution_cycle_time
  ON crophealth.ipm_execution(crop_cycle_id, executed_at DESC);

CREATE TRIGGER trg_ipm_plan_updated_at
BEFORE UPDATE ON crophealth.ipm_plan
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
