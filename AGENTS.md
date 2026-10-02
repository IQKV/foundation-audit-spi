# AI Agent Development Guide

## Project Overview

**Foundation Audit SPI** — Service Provider Interface for the iQ Foundation audit logging subsystem. Defines the contracts that audit backends (PostgreSQL, Elasticsearch, etc.) and Spring MVC web infrastructure must implement. Depends on `foundation-audit-model`; has no business logic of its own.

**Key characteristics:**

- Java 25, single Maven module, parent `com.iqkv:boot-parent-pom`
- Defines interfaces and thread-local context infrastructure — no concrete implementations
- Spring Data Commons + Spring WebMVC (`optional`) as the only Spring dependencies
- ArchUnit tests verify package structure
- Checkstyle enforced at `validate` phase via `com.iqkv:checkstyle-config`
- JaCoCo: ≥ 90% instruction + line + branch coverage per class (`jacoco.skip=true` by default; CI enables it)

## Dependency Relationship

```
foundation-audit-model   (data types: AuditEvent, AuditActor, enums)
        ↓
foundation-audit-spi     (contracts: interfaces + context infrastructure)
        ↓
foundation-audit-service (concrete implementation: Spring Boot, persistence)
```

Changes to `foundation-audit-model` may cascade here — read both before editing either.

## Project Structure

```
src/main/java/com/iqkv/foundation/audit/spi/
├── package-info.java
├── ActivityLogRepository.java    # Repository contract for querying stored audit logs
├── AuditEventPublisher.java      # Contract for publishing events to external systems (e.g., RabbitMQ)
├── AuditLogService.java          # Primary interface: log(AuditEvent)
├── AuditProvider.java            # Marker interface for backend provider identity
└── context/
    ├── AuditContextHolder.java       # ThreadLocal holder for AuditActor (IP, User-Agent)
    ├── AuditContextInterceptor.java  # Spring MVC interceptor — populates AuditContextHolder per request
    └── AuditEventEnricher.java       # Contract for enriching AuditEvent before dispatch
```

## Design Rules

### What belongs here

- Interfaces that define contracts for audit backends (`AuditLogService`, `AuditEventPublisher`, `ActivityLogRepository`)
- Thread-local context infrastructure (`AuditContextHolder`)
- Spring MVC interceptor that populates the context (`AuditContextInterceptor`)
- Enrichment contracts (`AuditEventEnricher`)

### What does NOT belong here

- Concrete implementations (database queries, message broker calls, Spring `@Component` beans)
- Business logic — this is a contract layer
- New Spring dependencies beyond `spring-data-commons` and `spring-webmvc` without explicit approval
- Persistence annotations (JPA, Hibernate)

### ThreadLocal contract (`AuditContextHolder`)

`AuditContextHolder` uses `ThreadLocal<AuditActor>`. Callers must:
1. Set context at request entry (`AuditContextInterceptor.preHandle`)
2. Clear context at request exit (`AuditContextInterceptor.afterCompletion`) — **never skip the clear**
3. Never pass the holder across async boundaries without explicit propagation

### Interface stability

All interfaces here are public contracts consumed by `foundation-audit-service` and potentially by external services. Adding a method to an existing interface is a **breaking change**. Use a new interface or a default method with a `UnsupportedOperationException` body and document the migration path.

### Javadoc

Every public interface, method, and class requires Javadoc. For interfaces, document each method's contract including thread-safety and null handling.

## Code Standards

```java
// Interface — document the contract, thread-safety, and null behaviour
/**
 * Primary interface for logging audit events.
 *
 * <p>Implementations must be thread-safe. A null event must throw
 * {@link IllegalArgumentException}.
 */
public interface AuditLogService {

  /**
   * Logs an audit event.
   *
   * @param event the audit event to log; must not be null
   */
  void log(AuditEvent event);
}

// ThreadLocal utility — always clear in finally or interceptor afterCompletion
public static void clearContext() {
  CONTEXT.remove();  // remove(), not set(null) — avoids memory leaks in thread pools
}
```

## Execution Discipline

- Read `foundation-audit-model` and `foundation-audit-service` before changing any interface here — both are affected.
- Adding a method to an existing interface is breaking — check all known implementations first.
- `AuditContextHolder.clearContext()` must always be called in a `finally` block or `afterCompletion` — verify any new caller does this.
- After two identical build failures without new evidence, change approach — do not retry blindly.
- Run `./mvnw verify -Djacoco.skip=false` to include coverage gates when touching context classes.

## Security

- `AuditContextHolder` carries client IP and User-Agent — treat as sensitive; never log at DEBUG in examples.
- `AuditContextInterceptor` extracts data from the HTTP request — validate and sanitise inputs; do not store raw header values without trimming.
- No credentials, tokens, or real PII in source, tests, or Javadoc examples.

## AI Agent Development Guidelines

### Code generation principles

1. **Contracts only**: no implementations, no Spring `@Component`/`@Service` beans
2. **Default methods for evolution**: prefer `default` methods with explicit `UnsupportedOperationException` over breaking existing interfaces
3. **ThreadLocal discipline**: every `set` has a paired `remove` in `finally` or `afterCompletion`
4. **Javadoc contracts**: document thread-safety, null handling, and lifecycle for every interface method

### Approval workflow (MANDATORY)

Ask before applying. For any file creation, modification, or deletion:

```
1. ANALYZE  → understand the request, read all known implementors and callers
2. PRESENT  → describe changes, show interface signature, list affected consumers
3. WAIT     → stop and wait for explicit approval
4. APPLY    → only after approval
5. VERIFY   → run ./mvnw verify, report results concisely
```

**Approval phrases:** "Yes", "Proceed", "Apply", "Do it", "Go ahead", "Looks good"

Operations NOT requiring approval: reading files, explaining structure, running type checks.

## Commit Standards

Format: `type(scope): subject`

- Subject: imperative, lowercase, no trailing period, ≤ 72 chars
- Types: `feat`, `fix`, `improvement`, `refactor`, `docs`, `test`, `chore`, `ci`, `revert`
- Scope: affected interface or package (e.g., `audit-log-service`, `context`, `enricher`, `publisher`)
- For `fix`: describe the symptom and trigger, not the code change
  - ✅ `fix(context): thread-local not cleared after async dispatch causes actor leak`
  - ❌ `fix(context): add remove() call in afterCompletion`

Examples:
- `feat(audit-log-service): add batch log method for bulk event ingestion`
- `fix(context-interceptor): actor not cleared when response is committed early`
- `refactor(audit-provider): extract getName into separate NamedProvider interface`
- `docs(audit-event-publisher): clarify at-least-once delivery guarantee in Javadoc`

## Development Commands

```bash
# Build, Checkstyle, and tests (coverage gate disabled by default)
./mvnw verify

# Include JaCoCo coverage gates
./mvnw verify -Djacoco.skip=false

# Checkstyle only
./mvnw checkstyle:check

# Skip Checkstyle for a quick compile check
./mvnw verify -Dcheckstyle.skip=true
```
