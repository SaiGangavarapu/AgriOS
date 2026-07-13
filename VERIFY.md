# Verification — Patch 012

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_012_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/04-srs/README.md",
  "docs/04-srs/SRS_DOCUMENT_CONTROL_v0.1.md",
  "docs/04-srs/SRS_01_ACTOR_CATALOGUE_v0.1.md",
  "docs/04-srs/SRS_02_USE_CASE_CATALOGUE_v0.1.md",
  "docs/04-srs/SRS_03_REQUIREMENT_CONVENTIONS_v0.1.md",
  "docs/04-srs/SRS_04_NON_FUNCTIONAL_REQUIREMENT_CONVENTIONS_v0.1.md",
  "docs/04-srs/SRS_05_ACCEPTANCE_CRITERIA_CONVENTIONS_v0.1.md",
  "docs/04-srs/SRS_06_FARMER_ONBOARDING_v0.1.md",
  "docs/04-srs/SRS_07_FARMER_PROFILE_AND_VERIFICATION_v0.1.md",
  "docs/04-srs/SRS_08_HOUSEHOLD_AND_ORGANIZATION_v0.1.md",
  "docs/04-srs/SRS_09_FARM_REGISTRATION_v0.1.md",
  "docs/04-srs/SRS_10_FIELD_AND_BOUNDARY_MANAGEMENT_v0.1.md",
  "docs/04-srs/SRS_11_TENURE_AND_CULTIVATION_RIGHTS_v0.1.md",
  "docs/04-srs/SRS_12_ROLES_AND_DELEGATED_ACCESS_v0.1.md",
  "docs/04-srs/SRS_13_CONSENT_AND_DATA_SHARING_v0.1.md",
  "docs/04-srs/SRS_14_VALIDATION_AND_ERROR_CATALOGUE_v0.1.md",
  "docs/04-srs/SRS_15_ACCEPTANCE_CRITERIA_v0.1.md",
  "docs/04-srs/SRS_TRACEABILITY_MATRIX_PART_1_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 012 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 012 verification passed." -ForegroundColor Green
```
