CREATE SCHEMA IF NOT EXISTS service;

CREATE TABLE service.service_provider (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  expert_profile_id uuid NULL REFERENCES advisory.expert_profile(id),
  user_account_id uuid NULL REFERENCES identity.user_account(id),
  provider_type varchar(40) NOT NULL CHECK (provider_type IN ('VET','AGRONOMIST','CROP_EXPERT','SOIL_EXPERT','IRRIGATION_EXPERT','FEED_SELLER','INPUT_SELLER','EQUIPMENT_SERVICE','OTHER')),
  display_name varchar(200) NOT NULL,
  organization_name varchar(240) NULL,
  phone_number varchar(40) NULL,
  email_address varchar(200) NULL,
  registration_reference varchar(160) NULL,
  service_area_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  language_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  service_codes jsonb NOT NULL DEFAULT '[]'::jsonb,
  consultation_mode varchar(30) NOT NULL DEFAULT 'BOTH' CHECK (consultation_mode IN ('IN_PERSON','REMOTE','BOTH')),
  status varchar(30) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED')),
  average_rating numeric(3,2) NOT NULL DEFAULT 0,
  review_count integer NOT NULL DEFAULT 0,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE service.provider_availability (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  provider_id uuid NOT NULL REFERENCES service.service_provider(id) ON DELETE CASCADE,
  start_at timestamptz NOT NULL,
  end_at timestamptz NOT NULL,
  mode varchar(30) NOT NULL CHECK (mode IN ('IN_PERSON','REMOTE')),
  location_text varchar(500) NULL,
  capacity integer NOT NULL DEFAULT 1 CHECK (capacity > 0),
  booked_count integer NOT NULL DEFAULT 0 CHECK (booked_count >= 0),
  status varchar(20) NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE','BLOCKED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_provider_availability_time CHECK (end_at > start_at),
  CONSTRAINT ck_provider_availability_capacity CHECK (booked_count <= capacity)
);

CREATE TABLE service.appointment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  provider_id uuid NOT NULL REFERENCES service.service_provider(id),
  availability_id uuid NULL REFERENCES service.provider_availability(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  subject_type varchar(50) NOT NULL DEFAULT 'FARM',
  subject_reference_id uuid NULL,
  appointment_type varchar(60) NOT NULL,
  mode varchar(30) NOT NULL CHECK (mode IN ('IN_PERSON','REMOTE')),
  scheduled_start_at timestamptz NOT NULL,
  scheduled_end_at timestamptz NOT NULL,
  location_text varchar(500) NULL,
  problem_summary text NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'REQUESTED' CHECK (status IN ('REQUESTED','CONFIRMED','IN_PROGRESS','COMPLETED','REJECTED','CANCELLED','NO_SHOW')),
  cancellation_reason text NULL,
  requested_by uuid NULL,
  provider_notes text NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_appointment_time CHECK (scheduled_end_at > scheduled_start_at)
);

CREATE TABLE service.appointment_visit_note (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  appointment_id uuid NOT NULL REFERENCES service.appointment(id) ON DELETE CASCADE,
  provider_id uuid NOT NULL REFERENCES service.service_provider(id),
  diagnosis_summary text NULL,
  recommendation_text text NOT NULL,
  prescription_details jsonb NOT NULL DEFAULT '[]'::jsonb,
  follow_up_required boolean NOT NULL DEFAULT false,
  follow_up_due_at timestamptz NULL,
  attachment_references jsonb NOT NULL DEFAULT '[]'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE service.provider_review (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  appointment_id uuid NOT NULL UNIQUE REFERENCES service.appointment(id),
  provider_id uuid NOT NULL REFERENCES service.service_provider(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  rating integer NOT NULL CHECK (rating BETWEEN 1 AND 5),
  review_text text NULL,
  status varchar(20) NOT NULL DEFAULT 'PUBLISHED' CHECK (status IN ('PUBLISHED','HIDDEN','FLAGGED')),
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_service_provider_lookup ON service.service_provider(tenant_id, provider_type, status);
CREATE INDEX ix_provider_availability_lookup ON service.provider_availability(tenant_id, provider_id, start_at, status);
CREATE INDEX ix_appointment_farmer ON service.appointment(tenant_id, farmer_id, scheduled_start_at);
CREATE INDEX ix_appointment_provider ON service.appointment(tenant_id, provider_id, status, scheduled_start_at);
CREATE TRIGGER trg_service_provider_updated_at BEFORE UPDATE ON service.service_provider FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
CREATE TRIGGER trg_service_appointment_updated_at BEFORE UPDATE ON service.appointment FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
