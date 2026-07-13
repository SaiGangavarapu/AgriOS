# Contributing to AgriOS

## Documentation rules

- Use Markdown for all baseline specifications.
- Keep files versioned through Git history.
- Use explicit assumptions and avoid unsupported agronomy claims.
- Cite official or peer-reviewed sources in research documents.
- Mark recommendations as Draft, Expert Reviewed, Approved, Superseded, or Withdrawn.
- Separate farmer-facing guidance from internal technical design.
- Prefer field-specific recommendations over generic crop advice.

## Engineering rules

- Java 21 and Spring Boot 3.x for backend modules.
- React and TypeScript for web and PWA clients.
- PostgreSQL and PostGIS for transactional and spatial data.
- Flyway for schema changes.
- DTOs must be used at API boundaries.
- No placeholder implementations in production modules.
- All decisions that affect architecture must be recorded under `decisions/`.
