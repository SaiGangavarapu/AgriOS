CREATE SCHEMA IF NOT EXISTS seed;

CREATE TABLE seed.seed_lot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  lot_code varchar(100) NOT NULL,
  seed_class varchar(50) NOT NULL,
  supplier_name varchar(200) NULL,
  producer_name varchar(200) NULL,
  germination_percent numeric(8,4) NULL
    CHECK (germination_percent IS NULL OR
           (germination_percent >= 0 AND germination_percent <= 100)),
  purity_percent numeric(8,4) NULL
    CHECK (purity_percent IS NULL OR
           (purity_percent >= 0 AND purity_percent <= 100)),
  treatment_status varchar(40) NOT NULL DEFAULT 'UNTREATED',
  quantity_available numeric(18,4) NOT NULL CHECK (quantity_available >= 0),
  quantity_unit varchar(40) NOT NULL,
  packed_on date NULL,
  expires_on date NULL,
  status varchar(30) NOT NULL DEFAULT 'AVAILABLE'
    CHECK (status IN ('AVAILABLE','QUARANTINED','EXHAUSTED','EXPIRED','REJECTED')),
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_seed_lot_tenant_code UNIQUE (tenant_id, lot_code),
  CONSTRAINT ck_seed_lot_dates CHECK (
    expires_on IS NULL OR packed_on IS NULL OR expires_on >= packed_on
  )
);

CREATE TABLE seed.germination_test (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  seed_lot_id uuid NOT NULL REFERENCES seed.seed_lot(id),
  tested_at timestamptz NOT NULL,
  sample_size integer NOT NULL CHECK (sample_size > 0),
  germinated_count integer NOT NULL CHECK (germinated_count >= 0),
  germination_percent numeric(8,4) NOT NULL,
  method varchar(100) NOT NULL,
  tested_by uuid NULL,
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT ck_germination_count CHECK (germinated_count <= sample_size)
);

CREATE TABLE seed.seed_treatment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  seed_lot_id uuid NOT NULL REFERENCES seed.seed_lot(id),
  treatment_type varchar(80) NOT NULL,
  product_name varchar(200) NULL,
  treatment_date date NOT NULL,
  treated_quantity numeric(18,4) NOT NULL CHECK (treated_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  notes text NULL,
  treated_by uuid NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE seed.seed_allocation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  seed_lot_id uuid NOT NULL REFERENCES seed.seed_lot(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  allocated_quantity numeric(18,4) NOT NULL CHECK (allocated_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  allocated_at timestamptz NOT NULL DEFAULT now(),
  allocated_by uuid NULL,
  status varchar(30) NOT NULL DEFAULT 'ALLOCATED'
    CHECK (status IN ('ALLOCATED','USED','RETURNED','CANCELLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_seed_allocation_cycle_lot UNIQUE (seed_lot_id, crop_cycle_id)
);

CREATE INDEX ix_seed_lot_crop_status
  ON seed.seed_lot(tenant_id, crop_id, status);

CREATE INDEX ix_seed_allocation_cycle
  ON seed.seed_allocation(crop_cycle_id, status);

CREATE TRIGGER trg_seed_lot_updated_at
BEFORE UPDATE ON seed.seed_lot
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
