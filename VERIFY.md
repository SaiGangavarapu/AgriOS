# Verification — Patch 005

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_005_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/BRD_22_SOIL_SAMPLING_AND_TESTING_v0.1.md",
  "docs/02-brd/BRD_23_WATER_SAMPLING_AND_QUALITY_v0.1.md",
  "docs/02-brd/BRD_24_CROP_AND_VARIETY_KNOWLEDGE_v0.1.md",
  "docs/02-brd/BRD_25_CROP_SUITABILITY_AND_RANKING_v0.1.md",
  "docs/02-brd/BRD_26_CROP_PLANNING_v0.1.md",
  "docs/02-brd/BRD_27_CROP_CYCLE_MANAGEMENT_v0.1.md",
  "docs/02-brd/BRD_28_GROWTH_STAGE_MANAGEMENT_v0.1.md",
  "docs/02-brd/BRD_29_SEED_AND_PLANTING_MATERIAL_v0.1.md",
  "docs/02-brd/BRD_30_FARM_OPERATIONS_v0.1.md",
  "docs/02-brd/BRD_31_CROP_CALENDAR_AND_TASKS_v0.1.md",
  "docs/02-brd/BRD_32_SEASON_CLOSURE_AND_LEARNING_v0.1.md",
  "docs/02-brd/BRD_33_VALIDATION_REPORTING_AND_AUDIT_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_PART_3_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 005 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 005 verification passed." -ForegroundColor Green
```
