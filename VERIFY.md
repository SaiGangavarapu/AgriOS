# Verification — Patch 007

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_007_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/BRD_46_PEST_AND_DISEASE_KNOWLEDGE_v0.1.md",
  "docs/02-brd/BRD_47_FIELD_SCOUTING_AND_OBSERVATIONS_v0.1.md",
  "docs/02-brd/BRD_48_IMAGE_BASED_CROP_HEALTH_OBSERVATIONS_v0.1.md",
  "docs/02-brd/BRD_49_DIAGNOSIS_CONFIDENCE_AND_ESCALATION_v0.1.md",
  "docs/02-brd/BRD_50_TREATMENT_PLANNING_AND_SAFETY_v0.1.md",
  "docs/02-brd/BRD_51_HARVEST_PLANNING_AND_EXECUTION_v0.1.md",
  "docs/02-brd/BRD_52_POST_HARVEST_GRADING_AND_STORAGE_v0.1.md",
  "docs/02-brd/BRD_53_MARKET_INTELLIGENCE_AND_BUYER_WORKFLOWS_v0.1.md",
  "docs/02-brd/BRD_54_FARM_ECONOMICS_AND_PROFITABILITY_v0.1.md",
  "docs/02-brd/BRD_55_INSURANCE_AND_CLAIM_SUPPORT_v0.1.md",
  "docs/02-brd/BRD_56_LENDING_AND_FINANCIAL_EVIDENCE_v0.1.md",
  "docs/02-brd/BRD_57_PRODUCE_LOT_TRACEABILITY_v0.1.md",
  "docs/02-brd/BRD_58_VALIDATION_REPORTING_AND_AUDIT_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_PART_5_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 007 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 007 verification passed." -ForegroundColor Green
```
