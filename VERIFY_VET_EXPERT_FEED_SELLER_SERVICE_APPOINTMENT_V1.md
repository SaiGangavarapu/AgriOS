# Verification

1. Confirm V047 is the latest migration.
2. Run `mvn clean verify` from `backend`.
3. Run Docker Compose and confirm Flyway applies V047.
4. Create an active provider.
5. Add availability.
6. Book an appointment against the slot.
7. Transition REQUESTED -> CONFIRMED -> IN_PROGRESS -> COMPLETED.
8. Add a visit note and farmer review.
9. Verify farmer history and provider dashboard.

No OpenAI or Ollama service is required.
