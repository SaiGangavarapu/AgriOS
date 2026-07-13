# Verification — Patch 003

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_003_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/README.md",
  "docs/02-brd/BRD_DOCUMENT_CONTROL_v0.1.md",
  "docs/02-brd/BRD_01_EXECUTIVE_SUMMARY_v0.1.md",
  "docs/02-brd/BRD_02_BUSINESS_CONTEXT_AND_PROBLEM_STATEMENT_v0.1.md",
  "docs/02-brd/BRD_03_VISION_GOALS_AND_OUTCOMES_v0.1.md",
  "docs/02-brd/BRD_04_STAKEHOLDERS_v0.1.md",
  "docs/02-brd/BRD_05_PERSONAS_v0.1.md",
  "docs/02-brd/BRD_06_SCOPE_AND_BOUNDARIES_v0.1.md",
  "docs/02-brd/BRD_07_BUSINESS_CAPABILITY_MAP_v0.1.md",
  "docs/02-brd/BRD_08_SUCCESS_MEASURES_AND_KPIS_v0.1.md",
  "docs/02-brd/BRD_09_ASSUMPTIONS_CONSTRAINTS_DEPENDENCIES_v0.1.md",
  "docs/02-brd/BRD_10_BUSINESS_RISKS_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 003 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 003 verification passed." -ForegroundColor Green
```
