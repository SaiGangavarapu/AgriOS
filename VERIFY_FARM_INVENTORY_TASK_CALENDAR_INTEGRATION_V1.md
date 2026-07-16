# Verification

1. Confirm V046 is the latest migration.
2. Run `mvn clean verify` from `backend`.
3. Start PostgreSQL and backend with Docker Compose.
4. Verify `/actuator/health` and `/actuator/flyway`.
5. Create an inventory item, receive a lot, and verify farm inventory.
6. Create a recurring task schedule and invoke due generation.
7. Query the work-management calendar and farm dashboard.

No local application, Maven success, Docker success, or runtime API verification is claimed until executed in the user's environment.
