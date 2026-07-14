# API 25 — OpenAPI Components

The root OpenAPI document under `openapi/openapi.yaml` defines:

- bearer authentication
- correlation and idempotency headers
- UUID, timestamp, quantity, money, confidence, evidence, page metadata, and error schemas
- representative farmer, farm, field, crop-plan, advisory, and device paths

During implementation, each module shall own a fragment under:

```text
openapi/paths/
openapi/schemas/
```

CI shall bundle and validate the fragments into one published contract.
