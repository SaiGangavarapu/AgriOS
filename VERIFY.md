# Verification — Patch 006

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_006_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/BRD_34_NUTRIENT_MANAGEMENT_PLANNING_v0.1.md",
  "docs/02-brd/BRD_35_INPUT_APPLICATION_AND_SAFETY_CONTROLS_v0.1.md",
  "docs/02-brd/BRD_36_IRRIGATION_PLANNING_AND_SCHEDULING_v0.1.md",
  "docs/02-brd/BRD_37_WEATHER_DATA_AND_FORECAST_MANAGEMENT_v0.1.md",
  "docs/02-brd/BRD_38_MONSOON_AND_CLIMATE_RISK_ADVISORIES_v0.1.md",
  "docs/02-brd/BRD_39_IOT_DEVICE_REGISTRY_AND_INSTALLATION_v0.1.md",
  "docs/02-brd/BRD_40_TELEMETRY_AND_DATA_QUALITY_v0.1.md",
  "docs/02-brd/BRD_41_DEVICE_ALERTS_MAINTENANCE_AND_CALIBRATION_v0.1.md",
  "docs/02-brd/BRD_42_AUTOMATION_AND_ACTUATOR_SAFEGUARDS_v0.1.md",
  "docs/02-brd/BRD_43_ADVISORY_GENERATION_AND_EXPLANATION_v0.1.md",
  "docs/02-brd/BRD_44_EXPERT_REVIEW_AND_CONSULTATION_v0.1.md",
  "docs/02-brd/BRD_45_VALIDATION_REPORTING_AND_AUDIT_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_PART_4_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 006 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 006 verification passed." -ForegroundColor Green
```
