# What Changed — Spring AI Foundation v1.0

- Updated Spring Boot 3.3.5 to 3.4.2 for supported Spring AI 1.0 compatibility; retained Java 21.
- Imported Spring AI BOM 1.0.0 and added only the Ollama model starter.
- Added provider-neutral chat SPI and tenant-aware provider/model router.
- Added conditional Ollama ChatClient adapter, deterministic mock provider, and OpenAI-ready interface/configuration.
- Added prompt-template repository/resolver with language and safe fallback behavior.
- Added basic input/output and suspicious instruction/action screening foundations.
- Added assistant submit orchestration and API response metadata.
- Persisted user and assistant messages with latency, token fields, model, finish reason, provider, prompt-template, and correlation metadata where available.
- Added mock profile and focused unit tests.
- Added no database migration.
