# Local Startup

From the repository root:

```powershell
Copy-Item "database\.env.example" "database\.env" -Force
docker compose --env-file "database\.env" -f "database\docker-compose.yml" up -d
docker compose --env-file "database\.env" -f "database\docker-compose.yml" ps
```

The migration directory is mounted into the PostgreSQL initialization folder.
For a clean rebuild, remove the volume only when local data can be discarded.
