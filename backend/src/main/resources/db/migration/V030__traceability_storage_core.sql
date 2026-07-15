CREATE TABLE traceability.trace_lot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  lot_code varchar(120) NOT NULL,
  harvest_batch_id uuid NOT NULL REFERENCES harvest.harvest_batch(id),
  crop_cycle_id uuid NOT NULL REFERENCES cropcycle.crop_cycle(id),
  field_id uuid NOT NULL REFERENCES farm.field(id),
  quality_assessment_id uuid NULL REFERENCES yieldquality.quality_assessment(id),
  quality_grade varchar(40) NULL,
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  packed_at timestamptz NULL,
  expiry_date date NULL,
  status varchar(30) NOT NULL DEFAULT 'CREATED'
    CHECK (status IN ('CREATED','GRADED','PACKED','STORED','DISPATCHED','SOLD','RECALLED','EXPIRED','CLOSED')),
  qr_token varchar(180) NOT NULL,
  trace_snapshot jsonb NOT NULL DEFAULT '{}'::jsonb,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_trace_lot_code UNIQUE (tenant_id, lot_code),
  CONSTRAINT uq_trace_lot_qr UNIQUE (qr_token)
);

CREATE TABLE traceability.package (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  trace_lot_id uuid NOT NULL REFERENCES traceability.trace_lot(id),
  package_code varchar(120) NOT NULL,
  package_type varchar(40) NOT NULL
    CHECK (package_type IN ('BAG','BOX','SACK','CRATE','DRUM','CONTAINER','BOTTLE','POUCH','BULK')),
  packed_quantity numeric(18,4) NOT NULL CHECK (packed_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  packed_at timestamptz NOT NULL,
  best_before_date date NULL,
  status varchar(30) NOT NULL DEFAULT 'PACKED'
    CHECK (status IN ('PACKED','STORED','DISPATCHED','DELIVERED','DAMAGED','RECALLED','EXPIRED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_package_code UNIQUE (tenant_id, package_code)
);

CREATE TABLE traceability.storage_location (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  location_code varchar(100) NOT NULL,
  location_name varchar(200) NOT NULL,
  location_type varchar(40) NOT NULL
    CHECK (location_type IN ('WAREHOUSE','COLD_ROOM','SHED','SILO','PROCESSING_UNIT','TRANSIT')),
  temperature_controlled boolean NOT NULL DEFAULT false,
  humidity_controlled boolean NOT NULL DEFAULT false,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_storage_location_code UNIQUE (tenant_id, location_code)
);

CREATE TABLE traceability.storage_movement (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  trace_lot_id uuid NOT NULL REFERENCES traceability.trace_lot(id),
  package_id uuid NULL REFERENCES traceability.package(id),
  from_location_id uuid NULL REFERENCES traceability.storage_location(id),
  to_location_id uuid NOT NULL REFERENCES traceability.storage_location(id),
  moved_at timestamptz NOT NULL,
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  movement_type varchar(40) NOT NULL
    CHECK (movement_type IN ('RECEIPT','TRANSFER','DISPATCH','RETURN','ADJUSTMENT')),
  reference_type varchar(80) NULL,
  reference_id uuid NULL,
  moved_by uuid NULL,
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE traceability.recall_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  trace_lot_id uuid NOT NULL REFERENCES traceability.trace_lot(id),
  recall_code varchar(120) NOT NULL,
  reason varchar(500) NOT NULL,
  severity varchar(20) NOT NULL
    CHECK (severity IN ('LOW','MODERATE','HIGH','CRITICAL')),
  initiated_at timestamptz NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','IN_PROGRESS','COMPLETED','CANCELLED')),
  initiated_by uuid NULL,
  completed_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_recall_code UNIQUE (tenant_id, recall_code)
);

CREATE INDEX ix_trace_lot_cycle
  ON traceability.trace_lot(crop_cycle_id, created_at DESC);

CREATE INDEX ix_storage_movement_lot_time
  ON traceability.storage_movement(trace_lot_id, moved_at DESC);

CREATE TRIGGER trg_trace_lot_updated_at
BEFORE UPDATE ON traceability.trace_lot
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
