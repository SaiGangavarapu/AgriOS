# Verification — Patch 008

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "PATCH_008_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/02-brd/BRD_59_MULTILINGUAL_ACCESSIBILITY_AND_LOW_LITERACY_UX_v0.1.md",
  "docs/02-brd/BRD_60_VOICE_AND_CONVERSATIONAL_INTERACTION_v0.1.md",
  "docs/02-brd/BRD_61_OFFLINE_FIRST_AND_SYNCHRONIZATION_v0.1.md",
  "docs/02-brd/BRD_62_NOTIFICATIONS_AND_COMMUNICATION_v0.1.md",
  "docs/02-brd/BRD_63_IDENTITY_AUTHENTICATION_AND_ACCOUNT_PROTECTION_v0.1.md",
  "docs/02-brd/BRD_64_AUTHORIZATION_AND_SEGREGATION_OF_DUTIES_v0.1.md",
  "docs/02-brd/BRD_65_PRIVACY_RETENTION_AND_DATA_SUBJECT_RIGHTS_v0.1.md",
  "docs/02-brd/BRD_66_SECURITY_MONITORING_AND_INCIDENT_RESPONSE_v0.1.md",
  "docs/02-brd/BRD_67_COMPLIANCE_POLICY_AND_PROGRAMME_ADMINISTRATION_v0.1.md",
  "docs/02-brd/BRD_68_PLATFORM_CONFIGURATION_AND_MASTER_DATA_v0.1.md",
  "docs/02-brd/BRD_69_ANALYTICS_RESEARCH_AND_DATA_GOVERNANCE_v0.1.md",
  "docs/02-brd/BRD_70_SUPPORT_SERVICE_MANAGEMENT_AND_FIELD_OPERATIONS_v0.1.md",
  "docs/02-brd/BRD_71_AVAILABILITY_CONTINUITY_BACKUP_AND_RECOVERY_v0.1.md",
  "docs/02-brd/BRD_72_PERFORMANCE_SCALABILITY_AND_INTEROPERABILITY_v0.1.md",
  "docs/02-brd/BRD_73_BRD_COMPLETION_REVIEW_AND_OPEN_DECISIONS_v0.1.md",
  "docs/02-brd/BRD_TRACEABILITY_REGISTER_PART_6_v0.1.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Patch 008 verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Patch 008 verification passed." -ForegroundColor Green
```
