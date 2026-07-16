# AgriOS Spring AI Foundation v1.0 — Implementation Index

## Baseline inspection
- Java: 21
- Spring Boot baseline: 3.3.5
- Spring Boot selected: 3.4.2, required by the supported Spring AI 1.0 line
- Spring AI BOM: 1.0.0
- Maven structure: one backend Maven module (`backend/pom.xml`)
- Existing AI package: `com.agrios.platform.ai` with API, application, and domain layers
- Existing migrations: V001 through V045; AI schema is V043–V045
- New migration: none

## Added architecture
- `foundation/provider`: application-facing provider-neutral chat SPI, conditional Ollama adapter, deterministic mock adapter, OpenAI-ready boundary.
- `foundation/application`: tenant-aware model/provider routing and request orchestration.
- `foundation/prompt`: database prompt lookup with language fallback and safe fallback prompt.
- `foundation/guardrail`: input length, injection-language, restricted-action-language, and output length foundations.
- `foundation/config`: typed AI configuration.
- API: `POST /api/v1/ai/assistant/sessions/{sessionId}/submit`.

## Data flow
1. Resolve tenant and correlation ID.
2. Validate active assistant session and input guardrails.
3. Resolve requested/default CHAT model from `ai.ai_model_profile` and provider from `ai.ai_provider`.
4. Resolve active prompt by assistant type and session language, falling back to English and then a safe built-in template.
5. Load bounded conversation history.
6. Persist the user message.
7. Invoke the selected provider-neutral adapter through Spring AI `ChatClient` for Ollama.
8. Validate response length.
9. Persist assistant text plus model, latency, token fields when supplied, finish reason, provider, template, and correlation metadata.
10. Return both message IDs and provider/model metadata.

## Supported assistant types
GENERAL, CROP_PLANNING, IRRIGATION, NUTRIENT, PEST_DISEASE, WEATHER, MARKET, FINANCE, COMPLIANCE, ORGANIZATION.

## Configuration examples
### Local Ollama
```powershell
$env:SPRING_AI_CHAT_MODEL = "ollama"
$env:AGRIOS_AI_OLLAMA_ENABLED = "true"
$env:OLLAMA_BASE_URL = "http://localhost:11434"
$env:OLLAMA_CHAT_MODEL = "llama3.2:3b"
```
The tenant must also have an ACTIVE OLLAMA provider and enabled CHAT model profile, preferably marked `default_for_role=true`.

### OpenAI-ready environment
```powershell
$env:OPENAI_API_KEY = "<set-locally-or-in-secret-manager>"
$env:OPENAI_CHAT_MODEL = "gpt-4.1-mini"
$env:AGRIOS_AI_OPENAI_ENABLED = "false"
```
The production OpenAI client adapter is intentionally deferred; no key is committed.

### Deterministic test provider
```powershell
$env:SPRING_PROFILES_ACTIVE = "ai-mock"
$env:AGRIOS_AI_MOCK_ENABLED = "true"
```

## Architecture conflicts found
- Spring Boot 3.3.5 is outside the supported Spring AI 1.0 baseline; updated narrowly to 3.4.2.
- Existing provider/model rows are tenant-scoped, so an enabled adapter alone is insufficient; database configuration is required for live routing.
- Existing message table has no dedicated provider column; provider and correlation data are stored in message JSON metadata, while model/tokens/latency/finish reason use existing columns.
- Retry/timeout properties are defined, but production-grade per-provider retry classification and HTTP timeout enforcement remain in the next provider-adapters milestone.

## Known limitations
No streaming, embeddings, vector retrieval, reranking, citations generation, executable domain tools, provider failover, cost capture, or complete prompt-injection security. Ollama token metadata is not captured by this initial ChatClient adapter. OpenAI is configuration/adapter-ready but not live.

## Next milestones
1. Ollama and OpenAI Production Provider Adapters v1.0
2. pgvector, Embeddings, Hybrid Retrieval, and Citations v1.0
3. Executable AgriOS Domain Tools v1.0
4. Farmer Assistant Orchestration and Streaming v1.0
5. AI Security, Observability, Evaluation, and Cost Controls v1.0
