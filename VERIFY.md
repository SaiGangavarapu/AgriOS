# Verification — Patch 011

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_011_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/03-domain-architecture/UBIQUITOUS_LANGUAGE_v0.1.md",
  "docs/03-domain-architecture/AGGREGATE_STATE_MODELS_v0.1.md",
  "docs/03-domain-architecture/COMMAND_ACCEPTANCE_CRITERIA_v0.1.md",
  "docs/03-domain-architecture/EVENT_CONTRACTS_AND_ENVELOPES_v0.1.md",
  "docs/03-domain-architecture/MODULE_DEPENDENCY_RULES_v0.1.md",
  "docs/03-domain-architecture/ARCHITECTURE_TEST_STRATEGY_v0.1.md",
  "docs/03-domain-architecture/DOMAIN_ARCHITECTURE_COMPLETION_REVIEW_v0.1.md",
  "docs/03-domain-architecture/DOMAIN_ARCHITECTURE_TRACEABILITY_PART_3_v0.1.md",
  "decisions/ADR-0003-separate-farmer-and-user-identities.md",
  "decisions/ADR-0004-multi-tenant-programme-aware-model.md",
  "decisions/ADR-0005-domain-owned-tasks-with-central-task-projection.md",
  "decisions/ADR-0006-advisory-context-does-not-own-domain-rules.md",
  "decisions/ADR-0007-telemetry-as-separate-bounded-context.md",
  "decisions/ADR-0008-existing-products-integrate-through-contracts.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 011 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 011 verification passed." -ForegroundColor Green
```
