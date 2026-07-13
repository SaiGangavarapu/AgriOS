# Verification — Patch 009

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_009_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/03-domain-architecture/DOMAIN_ARCHITECTURE_OVERVIEW_v0.1.md",
  "docs/03-domain-architecture/SUBDOMAIN_CLASSIFICATION_v0.1.md",
  "docs/03-domain-architecture/BOUNDED_CONTEXT_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/CONTEXT_MAP_v0.1.md",
  "docs/03-domain-architecture/AGGREGATE_AND_ENTITY_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/DOMAIN_EVENT_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/DOMAIN_SERVICE_POLICY_SPECIFICATION_CATALOGUE_v0.1.md",
  "docs/03-domain-architecture/DATA_OWNERSHIP_AND_AUTHORITY_MATRIX_v0.1.md",
  "docs/03-domain-architecture/SHARED_KERNEL_AND_INTEGRATION_RULES_v0.1.md",
  "docs/03-domain-architecture/ANTI_CORRUPTION_LAYER_GUIDANCE_v0.1.md",
  "docs/03-domain-architecture/SPRING_MODULITH_MODULE_MAP_v0.1.md",
  "docs/03-domain-architecture/BRD_TO_CONTEXT_TRACEABILITY_v0.1.md",
  "docs/03-domain-architecture/OPEN_DOMAIN_DECISIONS_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 009 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 009 verification passed." -ForegroundColor Green
```
