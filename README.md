# Foundation Audit SPI 🔌

Service Provider Interface (SPI) for the IQKV platform's audit logging system. Defines the core contracts and interfaces that decouple domain services from specific audit storage and publishing implementations.

## About

The Audit SPI library enforces the "No Vendor Lock-in" philosophy for platform compliance:

- **Audit Storage Abstraction** — defines `AuditStore` and `AuditLogService` interfaces, allowing backends (PostgreSQL, Elasticsearch, etc.) to be swapped without changing business logic.
- **Publisher Contracts** — provides the `AuditEventPublisher` interface for services to emit audit events consistently.
- **Provider Discovery** — utilizes standard Java SPI or Spring Boot auto-configuration patterns to load the active audit provider at runtime.
- **Search & Retention API** — standardizes how audit logs are searched, filtered, and purged, regardless of the underlying data store.
- **Extensibility** — makes it easy for platform users to plug in custom audit providers (e.g., for integration with enterprise SIEM systems).

## Quick Links

- [API Documentation](./docs/api/README.md)
- [Architecture Overview](./docs/architecture/README.md)
- [Deployment Guide](./docs/deployment/README.md)
- [Contributing Guidelines](.github/CONTRIBUTING.md)

## Tech Stack

- Java 25
- Spring Data Commons (for pagination contracts)
- Maven 3.9+

## Development

```bash
# Build and install to local Maven repository
./mvnw clean install -Dcheckstyle.skip=true
```
