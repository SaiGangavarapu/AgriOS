CREATE INDEX ix_programme_tenant_status
  ON configuration.programme(tenant_id, status);

CREATE INDEX ix_farmer_tenant_status
  ON farmer.farmer(tenant_id, status);

CREATE INDEX ix_farmer_tenant_mobile
  ON farmer.farmer(tenant_id, mobile_e164)
  WHERE mobile_e164 IS NOT NULL;

CREATE INDEX ix_farmer_programme
  ON farmer.farmer(programme_id)
  WHERE programme_id IS NOT NULL;

CREATE INDEX ix_farm_tenant_operator
  ON farm.farm(tenant_id, primary_operator_farmer_id);

CREATE INDEX ix_field_farm_status
  ON farm.field(farm_id, status);

CREATE INDEX ix_tenure_field_status_dates
  ON tenure.tenure_arrangement(field_id, status, valid_from, valid_to);

CREATE INDEX ix_consent_active_lookup
  ON consent.consent_grant(farmer_id, purpose_code, recipient_type, recipient_id, status, valid_until);

CREATE INDEX ix_audit_tenant_time
  ON audit.audit_event(tenant_id, occurred_at DESC);

CREATE INDEX ix_audit_target
  ON audit.audit_event(target_type, target_id, occurred_at DESC);

CREATE INDEX ix_outbox_unpublished
  ON integration.outbox_event(created_at)
  WHERE published_at IS NULL;
