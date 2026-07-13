# Verification — Patch 004

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_004_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/BRD_11_FARMER_ONBOARDING_AND_IDENTITY_v0.1.md",
  "docs/02-brd/BRD_12_HOUSEHOLD_AND_ORGANIZATION_RELATIONSHIPS_v0.1.md",
  "docs/02-brd/BRD_13_FARM_REGISTRATION_v0.1.md",
  "docs/02-brd/BRD_14_FIELD_REGISTRATION_AND_BOUNDARIES_v0.1.md",
  "docs/02-brd/BRD_15_LAND_TENURE_AND_CULTIVATION_RIGHTS_v0.1.md",
  "docs/02-brd/BRD_16_WATER_SOURCE_MANAGEMENT_v0.1.md",
  "docs/02-brd/BRD_17_ROLES_AND_DELEGATED_ACCESS_v0.1.md",
  "docs/02-brd/BRD_18_CONSENT_AND_DATA_SHARING_v0.1.md",
  "docs/02-brd/BRD_19_FIELD_OFFICER_AND_ASSISTED_WORKFLOWS_v0.1.md",
  "docs/02-brd/BRD_20_VALIDATION_EXCEPTION_AND_LIFECYCLE_RULES_v0.1.md",
  "docs/02-brd/BRD_21_REPORTING_AUDIT_AND_ADMINISTRATION_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_PART_2_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 004 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 004 verification passed." -ForegroundColor Green
```
