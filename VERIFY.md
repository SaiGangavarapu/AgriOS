# Verification — Patch 010

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_010_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/03-domain-architecture/CONTEXT_SPECIFICATION_TEMPLATE_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_01_IDENTITY_FARMER_HOUSEHOLD_CONSENT_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_02_FARM_FIELD_TENURE_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_03_SOIL_WATER_AND_KNOWLEDGE_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_04_CROP_PLANNING_CYCLE_SEED_OPERATIONS_TASKS_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_05_NUTRIENT_IRRIGATION_WEATHER_IOT_TELEMETRY_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_06_ADVISORY_EXPERT_CROP_HEALTH_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_07_HARVEST_TRACEABILITY_MARKET_ECONOMICS_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_08_INSURANCE_LENDING_NOTIFICATION_LOCALIZATION_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_SPEC_09_CONFIGURATION_POLICY_DOCUMENT_AUDIT_SUPPORT_REPORTING_v0.1.md",
  "docs/03-domain-architecture/COMMAND_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/QUERY_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/REPOSITORY_AND_PORT_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/PROCESS_MANAGERS_AND_SAGAS_v0.1.md",
  "docs/03-domain-architecture/CONSISTENCY_IDEMPOTENCY_AND_FAILURE_HANDLING_v0.1.md",
  "docs/03-domain-architecture/DOMAIN_ARCHITECTURE_TRACEABILITY_PART_2_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 010 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 010 verification passed." -ForegroundColor Green
```
