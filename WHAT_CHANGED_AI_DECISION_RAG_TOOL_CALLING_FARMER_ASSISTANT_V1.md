# What Changed — AI Decision Intelligence, RAG, Tool Calling, and Farmer Assistant Core v1.0

Added the AI bounded context.

The implementation separates:

1. provider and model configuration
2. prompts and guardrails
3. RAG knowledge storage and retrieval traces
4. assistant conversations
5. tool discovery and execution governance
6. auditable decision records and citations

High-risk tools may require explicit confirmation. Tool requests are idempotent.
Knowledge is scoped by tenant, farmer, farm, field, and crop cycle to prevent
cross-user retrieval.
