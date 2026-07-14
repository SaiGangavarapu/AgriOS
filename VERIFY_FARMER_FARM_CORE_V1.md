# Verify Farmer and Farm Core v1.0

```powershell
Set-Location "C:\Users\ganga\IdeaProjects\AgriOS\backend"
mvn clean verify
docker compose up -d --build
Invoke-RestMethod "http://localhost:8080/actuator/health"
```

All business API calls require:

```text
X-Tenant-Id: 00000000-0000-0000-0000-000000000001
```
