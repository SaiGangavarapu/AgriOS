# Verification

```powershell
Set-Location "C:\Users\ganga\IdeaProjects\AgriOS\backend"

docker version
java -version
mvn -version

mvn clean verify

docker compose down
docker compose up -d --build
docker compose ps

Invoke-RestMethod "http://localhost:8080/actuator/health"
```

Verify Flyway applied migrations V001 through V010.

All business APIs require:

`X-Tenant-Id: 00000000-0000-0000-0000-000000000001`
