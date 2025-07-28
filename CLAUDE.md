# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Micronaut-based medication scheduling service built with Kotlin, designed as part of a microservices architecture for healthcare management. The service handles patient medication schedules, alerts, and time-based care coordination.

## Development Commands

### Build and Run
- `./gradlew run` - Start the application (runs on port 8080)
- `./gradlew build` - Build the project
- `./gradlew clean build` - Clean build

### Testing
- `./gradlew test` - Run all tests
- `./gradlew test --tests "ClassName"` - Run specific test class
- `./gradlew test --tests "ClassName.methodName"` - Run specific test method

## Architecture

### Core Components
- **Controllers** (`controller/`): REST API endpoints with validation
- **Services** (`service/`): Business logic layer with coroutine support
- **Models** (`model/`): Data classes with Micronaut Serde annotations
- **Repositories** (`repository/`): Data access interfaces (DynamoDB integration planned)

### Key Design Patterns
- **Dependency Injection**: Uses Micronaut's built-in DI with `@Singleton` and constructor injection
- **Async/Await**: Controllers and services use Kotlin coroutines (`suspend` functions)
- **Repository Pattern**: Interfaces define data access contracts

### Data Models
- **Schedule**: Patient daily medication schedules with timing information
- **Alert**: Notifications for overdue, missed, or upcoming medications with priority levels
- **Enums**: `AlertType`, `AlertPriority`, `AlertStatus` for type safety

### Configuration
- **DynamoDB Local**: Embedded DynamoDB for development/testing on port 8000
- **CORS**: Configured for Vite dev server (`localhost:5173`) with credentials support
- **Time Handling**: Uses `kotlinx-datetime` with UTC timezone
- **Validation**: Basic validation without annotation processing

### API Endpoints
- `GET /schedules/patients/{patientId}/{date}` - Retrieve patient schedule
- `GET /schedules/alerts` - Get active alerts
- `POST /schedules/alerts/acknowledge` - Acknowledge alert with validation

### Service Routing Strategy
Uses `/schedules` as the base path to avoid conflicts with other patient-related services. This allows multiple services to handle `/patients/*` endpoints under their own service-specific prefixes.

## Technology Stack
- **Framework**: Micronaut 4.2.1 with Netty runtime
- **Language**: Kotlin 1.9.20 with coroutines
- **Database**: AWS DynamoDB (Enhanced SDK)
- **Serialization**: Jackson with Micronaut Serde
- **Testing**: JUnit 5, MockK for Kotlin-friendly mocking
- **Build**: Gradle with Kotlin DSL

## Testing Strategy

### Local Testing (Mocked)
- **Repository Mocking**: In-memory mock implementations replace DynamoDB repositories
- **Test Isolation**: Each test uses fresh mock data (cleared in `@BeforeEach`)
- **Fast Execution**: No external dependencies or database setup required
- **MockK Integration**: Kotlin-friendly mocking for service layer testing

### Mock Repositories
- `MockScheduleRepository`: In-memory ConcurrentHashMap storage
- `MockAlertRepository`: In-memory ConcurrentHashMap storage
- Automatic replacement in test environment via `@Replaces` annotation

### Production Database
- **Development**: Requires external DynamoDB Local or AWS DynamoDB
- **Configuration**: `application.yml` points to DynamoDB endpoint
- **Credentials**: Uses dummy credentials for local development

## Gradle Best Practices

### Always Include These Base Dependencies
```gradle
// Configuration support
runtimeOnly("org.yaml:snakeyaml")

// Kotlin async support
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

// Logging implementation
runtimeOnly("ch.qos.logback:logback-classic")
```

### Testing Framework Selection
- Use JUnit 5 for Kotlin projects
- Use MockK instead of Mockito for Kotlin-friendly mocking
- Avoid mixing Groovy (Spock) with Kotlin projects
- Prefer in-memory mocks over external test dependencies