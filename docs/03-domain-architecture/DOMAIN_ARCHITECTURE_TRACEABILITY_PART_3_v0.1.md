# Domain Architecture Traceability — Part 3

| Architecture artifact | BRD coverage | Implementation implication |
|---|---|---|
| Ubiquitous language | BR-001–BR-340 | names for packages, APIs, events, and UI |
| Aggregate state models | lifecycle requirements | domain transition methods |
| Command acceptance criteria | validation and workflow requirements | application command handlers |
| Event envelope | audit, integration, traceability | outbox and event contracts |
| Module dependency rules | governance and interoperability | Spring Modulith modules |
| Architecture tests | quality and boundary requirements | CI enforcement |
| ADR-0003 | farmer onboarding and identity | separate farmer and user models |
| ADR-0004 | programme and institutional scope | tenant and programme identifiers |
| ADR-0005 | task and operation workflows | domain-owned tasks and shared projection |
| ADR-0006 | advisory governance | advisory assembles, does not invent rules |
| ADR-0007 | IoT and telemetry scale | separate telemetry ownership |
| ADR-0008 | related product integration | APIs/events rather than entity reuse |

## Completion status

- BRD baseline: complete for initial scope
- Domain map: complete for initial scope
- Context specifications: complete at starter level
- Architecture decisions: sufficient for SRS initiation
- Detailed API and data design: pending
