# Verify Crop Cycle, Seed, Operations, and Tasks v1.0

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

Verify Flyway applied V001 through V014.
