# Verify — Spring AI Foundation v1.0

## Required files
Verify `backend/pom.xml`, `backend/src/main/java/com/agrios/platform/ai/foundation`, the submit endpoint in `AiPlatformController`, test classes, configuration, and `docs/26-spring-ai-foundation/IMPLEMENTATION_INDEX_v1.0.md`.

## Build
```powershell
Set-Location "C:\Users\ganga\IdeaProjects\AgriOS\backend"
mvn clean verify
```

## Docker
```powershell
Set-Location "C:\Users\ganga\IdeaProjects\AgriOS\backend"
docker compose up -d --build
docker compose ps
docker compose logs --tail 200
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
```

## Ollama local example
Set the environment variables documented in the implementation index, ensure Ollama is running, and configure tenant-scoped ACTIVE provider/model rows. The backend must also start when Ollama is disabled.

## Acceptance checks
- Existing session/message APIs remain available.
- Submit endpoint persists USER then ASSISTANT messages.
- No configured provider returns a graceful 503 domain error.
- Mock tests require no OpenAI key or Ollama process.
- Flyway latest migration remains V045.
