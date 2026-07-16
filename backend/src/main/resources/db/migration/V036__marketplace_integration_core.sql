CREATE TABLE market.marketplace_connector (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  connector_code varchar(100) NOT NULL,
  connector_name varchar(240) NOT NULL,
  connector_type varchar(60) NOT NULL
    CHECK (connector_type IN ('AGRIOS_MARKETPLACE','SELLER_ERP','EXTERNAL_MARKETPLACE','PROCESSOR_PORTAL','EXPORT_PORTAL')),
  endpoint_reference varchar(500) NULL,
  authentication_reference varchar(240) NULL,
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','DEGRADED','SUSPENDED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_marketplace_connector_code UNIQUE (tenant_id, connector_code)
);

CREATE TABLE market.marketplace_listing (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  connector_id uuid NOT NULL REFERENCES market.marketplace_connector(id),
  sales_plan_id uuid NOT NULL REFERENCES market.sales_plan(id),
  trace_lot_id uuid NULL REFERENCES traceability.trace_lot(id),
  external_listing_id varchar(180) NULL,
  title varchar(300) NOT NULL,
  description text NULL,
  listed_quantity numeric(18,4) NOT NULL CHECK (listed_quantity > 0),
  available_quantity numeric(18,4) NOT NULL CHECK (available_quantity >= 0),
  quantity_unit varchar(40) NOT NULL,
  list_price numeric(18,4) NOT NULL CHECK (list_price >= 0),
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  fulfillment_mode varchar(40) NOT NULL
    CHECK (fulfillment_mode IN ('PICKUP','DELIVERY','BOTH','BULK_CONTRACT')),
  status varchar(30) NOT NULL DEFAULT 'DRAFT'
    CHECK (status IN ('DRAFT','PUBLISHING','ACTIVE','PAUSED','SOLD_OUT','EXPIRED','CANCELLED','FAILED')),
  published_at timestamptz NULL,
  last_synced_at timestamptz NULL,
  sync_error text NULL,
  idempotency_key varchar(180) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_marketplace_listing_idempotency UNIQUE (tenant_id, idempotency_key)
);

CREATE TABLE market.marketplace_order_reference (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  connector_id uuid NOT NULL REFERENCES market.marketplace_connector(id),
  marketplace_listing_id uuid NOT NULL REFERENCES market.marketplace_listing(id),
  external_order_id varchar(180) NOT NULL,
  ordered_quantity numeric(18,4) NOT NULL CHECK (ordered_quantity > 0),
  quantity_unit varchar(40) NOT NULL,
  unit_price numeric(18,4) NOT NULL,
  order_value numeric(18,2) NOT NULL,
  ordered_at timestamptz NOT NULL,
  status varchar(30) NOT NULL DEFAULT 'RECEIVED'
    CHECK (status IN ('RECEIVED','CONFIRMED','FULFILLING','DISPATCHED','DELIVERED','CANCELLED','RETURNED','REFUNDED')),
  financial_event_id uuid NULL REFERENCES finance.financial_event(id),
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_external_marketplace_order UNIQUE (connector_id, external_order_id)
);

CREATE TABLE market.price_alert (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  farmer_id uuid NOT NULL REFERENCES farmer.farmer(id),
  crop_id uuid NOT NULL REFERENCES knowledge.crop(id),
  variety_id uuid NULL REFERENCES knowledge.variety(id),
  market_location_id uuid NULL REFERENCES market.market_location(id),
  alert_type varchar(40) NOT NULL
    CHECK (alert_type IN ('PRICE_ABOVE','PRICE_BELOW','PRICE_CHANGE_PERCENT','BEST_MARKET')),
  threshold_value numeric(18,4) NULL,
  currency_code varchar(3) NOT NULL DEFAULT 'INR',
  quantity_unit varchar(40) NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  last_triggered_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_marketplace_listing_status
  ON market.marketplace_listing(connector_id, status, created_at DESC);

CREATE INDEX ix_marketplace_order_listing
  ON market.marketplace_order_reference(marketplace_listing_id, status, ordered_at DESC);

CREATE INDEX ix_price_alert_farmer
  ON market.price_alert(farmer_id, enabled, crop_id);

CREATE TRIGGER trg_marketplace_connector_updated_at
BEFORE UPDATE ON market.marketplace_connector
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
