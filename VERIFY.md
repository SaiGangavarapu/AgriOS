# Verification — Patch 002

From the repository root, run:

```powershell
$requiredFiles = @(
  "PATCH_002_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/00-project-governance/AGRONOMY_EVIDENCE_GOVERNANCE_v0.1.md",
  "docs/00-project-governance/AGRIOS_GLOSSARY_v0.1.md",
  "docs/01-research-foundation/FARMING_LIFECYCLE_AND_OPERATING_MODEL_v0.1.md",
  "docs/01-research-foundation/FARMING_SYSTEMS_COMPARISON_v0.1.md",
  "docs/01-research-foundation/SOIL_SCIENCE_AND_TEST_INTERPRETATION_v0.1.md",
  "docs/01-research-foundation/SEED_AND_VARIETY_SELECTION_v0.1.md",
  "docs/01-research-foundation/CROP_SUITABILITY_AND_PLANNING_v0.1.md",
  "docs/01-research-foundation/NUTRIENT_MANAGEMENT_STRATEGIES_v0.1.md",
  "docs/01-research-foundation/IRRIGATION_AND_WATER_MANAGEMENT_v0.1.md",
  "docs/01-research-foundation/WEATHER_AND_MONSOON_INTELLIGENCE_v0.1.md",
  "docs/01-research-foundation/IOT_AND_EDGE_AGRICULTURE_v0.1.md",
  "docs/14-roadmap/RESEARCH_VALIDATION_BACKLOG_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 002 verification passed." -ForegroundColor Green
```
