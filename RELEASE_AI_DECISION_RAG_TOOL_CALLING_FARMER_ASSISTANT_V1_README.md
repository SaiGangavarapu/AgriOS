# AgriOS AI Decision Intelligence, RAG, Tool Calling, and Farmer Assistant Core v1.0

Implemented:

- provider-neutral AI provider registry
- chat, embedding, vision, reranking, classification, and forecasting model profiles
- prompt-template and guardrail database foundations
- knowledge sources, documents, chunks, metadata, embeddings, and retrieval records
- farmer, farm, field, and crop-cycle knowledge scoping
- assistant sessions and conversation history
- multilingual assistant-session foundation
- tool definitions with JSON schemas
- tool risk levels and confirmation requirements
- idempotent tool-execution requests
- decision-record and response-citation database foundations
- Flyway migrations V043 through V045

This milestone intentionally avoids coupling the domain layer to one LLM vendor.
Spring AI, local Ollama, OpenAI, Azure OpenAI, and other providers can be added
as infrastructure adapters over these stable application contracts.
