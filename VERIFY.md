# Verification — Complete Database Design v1.0

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "RELEASE_DATABASE_DESIGN_V1_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/06-data-design/DB_INDEX_v1.0.md",
  "docs/06-data-design/DB_01_PRINCIPLES_AND_CONVENTIONS_v1.0.md",
  "docs/06-data-design/DB_02_SCHEMA_OWNERSHIP_v1.0.md",
  "docs/06-data-design/DB_03_GLOBAL_IDENTIFIERS_AND_AUDIT_COLUMNS_v1.0.md",
  "docs/06-data-design/DB_04_TENANT_AND_PROGRAMME_MODEL_v1.0.md",
  "docs/06-data-design/DB_05_IDENTITY_FARMER_HOUSEHOLD_ORGANIZATION_v1.0.md",
  "docs/06-data-design/DB_06_FARM_FIELD_TENURE_AND_WATER_SOURCE_v1.0.md",
  "docs/06-data-design/DB_07_SOIL_AND_WATER_LABORATORY_v1.0.md",
  "docs/06-data-design/DB_08_AGRONOMY_KNOWLEDGE_v1.0.md",
  "docs/06-data-design/DB_09_CROP_PLANNING_CYCLE_SEED_OPERATIONS_TASKS_v1.0.md",
  "docs/06-data-design/DB_10_NUTRIENT_AND_IRRIGATION_v1.0.md",
  "docs/06-data-design/DB_11_WEATHER_IOT_AND_TELEMETRY_v1.0.md",
  "docs/06-data-design/DB_12_ADVISORY_EXPERT_AND_CROP_HEALTH_v1.0.md",
  "docs/06-data-design/DB_13_HARVEST_STORAGE_AND_TRACEABILITY_v1.0.md",
  "docs/06-data-design/DB_14_MARKET_ECONOMICS_INSURANCE_AND_LENDING_v1.0.md",
  "docs/06-data-design/DB_15_CONSENT_AUDIT_LOCALIZATION_NOTIFICATION_SUPPORT_v1.0.md",
  "docs/06-data-design/DB_16_GEOSPATIAL_DESIGN_v1.0.md",
  "docs/06-data-design/DB_17_TIME_SERIES_DESIGN_v1.0.md",
  "docs/06-data-design/DB_18_OBJECT_STORAGE_METADATA_v1.0.md",
  "docs/06-data-design/DB_19_SEARCH_AND_REPORTING_PROJECTIONS_v1.0.md",
  "docs/06-data-design/DB_20_INDEXING_AND_PERFORMANCE_v1.0.md",
  "docs/06-data-design/DB_21_PARTITIONING_RETENTION_AND_ARCHIVAL_v1.0.md",
  "docs/06-data-design/DB_22_MIGRATION_AND_SEED_DATA_GOVERNANCE_v1.0.md",
  "docs/06-data-design/DB_23_DATA_SECURITY_PRIVACY_AND_MASKING_v1.0.md",
  "docs/06-data-design/DB_24_BACKUP_RESTORE_AND_RECONCILIATION_v1.0.md",
  "docs/06-data-design/DB_25_LOGICAL_RELATIONSHIP_MAP_v1.0.md",
  "docs/06-data-design/DB_26_DATABASE_TRACEABILITY_v1.0.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Database Design verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "AgriOS Complete Database Design v1.0 verification passed." -ForegroundColor Green
```
