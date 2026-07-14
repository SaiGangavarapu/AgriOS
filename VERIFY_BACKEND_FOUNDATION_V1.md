# Verify Backend Foundation v1.0

```powershell
Set-Location "C:\Users\ganga\IdeaProjects\AgriOS\backend"
mvn clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

For Docker:

```powershell
docker compose up -d --build
docker compose ps
Invoke-RestMethod http://localhost:8080/actuator/health
Invoke-RestMethod http://localhost:8080/api/v1/platform/info
```
