CREATE SCHEMA IF NOT EXISTS inventory;

CREATE TABLE inventory.inventory_item (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farm_id uuid NOT NULL REFERENCES farm.farm(id),
  item_code varchar(80) NOT NULL,
  item_name varchar(200) NOT NULL,
  category varchar(60) NOT NULL,
  base_unit varchar(40) NOT NULL,
  reorder_level numeric(18,4) NULL CHECK (reorder_level IS NULL OR reorder_level >= 0),
  current_quantity numeric(18,4) NOT NULL DEFAULT 0 CHECK (current_quantity >= 0),
  active boolean NOT NULL DEFAULT true,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL,
  CONSTRAINT uq_inventory_item_code UNIQUE (tenant_id, farm_id, item_code)
);

CREATE TABLE inventory.inventory_lot (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  inventory_item_id uuid NOT NULL REFERENCES inventory.inventory_item(id),
  lot_reference varchar(120) NOT NULL,
  received_date date NOT NULL,
  expiry_date date NULL,
  quantity_received numeric(18,4) NOT NULL CHECK (quantity_received > 0),
  quantity_available numeric(18,4) NOT NULL CHECK (quantity_available >= 0),
  unit_cost numeric(14,4) NULL CHECK (unit_cost IS NULL OR unit_cost >= 0),
  currency varchar(3) NOT NULL DEFAULT 'INR',
  supplier_reference varchar(160) NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_inventory_lot_reference UNIQUE (tenant_id, inventory_item_id, lot_reference)
);

CREATE TABLE inventory.stock_movement (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  inventory_item_id uuid NOT NULL REFERENCES inventory.inventory_item(id),
  inventory_lot_id uuid NULL REFERENCES inventory.inventory_lot(id),
  movement_type varchar(30) NOT NULL CHECK (movement_type IN ('RECEIPT','CONSUMPTION','ADJUSTMENT_IN','ADJUSTMENT_OUT','RETURN')),
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  unit_code varchar(40) NOT NULL,
  movement_date date NOT NULL,
  source_module varchar(60) NOT NULL,
  source_reference_type varchar(80) NOT NULL,
  source_reference_id uuid NULL,
  idempotency_key varchar(180) NOT NULL,
  notes text NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  CONSTRAINT uq_stock_movement_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE operations.operation_inventory_consumption (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  operation_id uuid NOT NULL REFERENCES operations.farm_operation(id) ON DELETE CASCADE,
  operation_input_id uuid NULL REFERENCES operations.operation_input(id) ON DELETE SET NULL,
  inventory_item_id uuid NOT NULL REFERENCES inventory.inventory_item(id),
  inventory_lot_id uuid NULL REFERENCES inventory.inventory_lot(id),
  stock_movement_id uuid NOT NULL REFERENCES inventory.stock_movement(id),
  quantity numeric(18,4) NOT NULL CHECK (quantity > 0),
  unit_code varchar(40) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_operation_inventory_movement UNIQUE (operation_id, stock_movement_id)
);

CREATE TABLE tasks.recurring_task_schedule (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  owner_context varchar(80) NOT NULL,
  owner_entity_id uuid NOT NULL,
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  task_type varchar(80) NOT NULL,
  title varchar(240) NOT NULL,
  description text NULL,
  priority varchar(20) NOT NULL DEFAULT 'NORMAL',
  assigned_to uuid NULL,
  recurrence_type varchar(20) NOT NULL CHECK (recurrence_type IN ('DAILY','WEEKLY','MONTHLY')),
  interval_value integer NOT NULL DEFAULT 1 CHECK (interval_value > 0),
  start_at timestamptz NOT NULL,
  next_run_at timestamptz NOT NULL,
  end_at timestamptz NULL,
  active boolean NOT NULL DEFAULT true,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE INDEX ix_inventory_item_farm_category ON inventory.inventory_item(tenant_id, farm_id, category, active);
CREATE INDEX ix_inventory_item_low_stock ON inventory.inventory_item(tenant_id, farm_id, current_quantity, reorder_level) WHERE active = true;
CREATE INDEX ix_inventory_lot_expiry ON inventory.inventory_lot(tenant_id, expiry_date, quantity_available) WHERE quantity_available > 0;
CREATE INDEX ix_stock_movement_item_date ON inventory.stock_movement(inventory_item_id, movement_date DESC);
CREATE INDEX ix_recurring_task_due ON tasks.recurring_task_schedule(tenant_id, active, next_run_at);
CREATE INDEX ix_task_tenant_due_status ON tasks.task(tenant_id, due_at, status);

CREATE TRIGGER trg_inventory_item_updated_at BEFORE UPDATE ON inventory.inventory_item
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
CREATE TRIGGER trg_recurring_task_schedule_updated_at BEFORE UPDATE ON tasks.recurring_task_schedule
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
