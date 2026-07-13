# SA 14 — AI, RAG, and Model Architecture

## AI gateway

Provides one controlled interface to hosted or local models.

## Capabilities

- natural-language interaction
- multilingual support
- tool calling
- RAG
- report extraction
- explanation
- summarization
- voice transcription
- text-to-speech

## Tool-calling pattern

```text
User Question
 → Intent and Context Resolution
 → Authorized Tool Selection
 → Domain Service Calls
 → Evidence and Result Assembly
 → LLM Explanation
 → Safety Check
 → Response
```

## RAG sources

- approved agronomy knowledge
- expert guidance
- policy
- laboratory interpretation references
- farmer-specific approved records

## Guardrails

- no invented agronomy
- no direct model write to domain tables
- no hidden tool execution
- confidence and source disclosure
- high-risk escalation
- prompt and output audit
- model evaluation and rollback
