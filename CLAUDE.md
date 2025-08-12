# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Micronaut-based medication scheduling service built with Kotlin, designed as part of a microservices architecture for healthcare management. The service handles patient medication schedules, alerts, and time-based care coordination.

## Service Startup Commands

### Prerequisites
- Java 17+
- Docker or Rancher Desktop (for DynamoDB Local)

### Quick Start

**✅ CURRENT STATUS: Service starts successfully with DynamoDB bean conflicts resolved**

#### Step 1: Test Service (Works)
```bash
# Run tests to verify service works
./gradlew test
```

#### Step 2: Start Docker/Rancher Desktop (Optional for testing without DynamoDB)
```bash
open -a "Rancher Desktop"
# Wait for Rancher Desktop to fully start (may take 30-60 seconds)
```

#### Step 3: Start DynamoDB Local and Create Table (Optional)
```bash
# Start DynamoDB Local (if not already running from other services)
docker-compose up -d dynamodb-local

# Create medication-scheduling table (if needed for production mode)
aws dynamodb create-table \
    --table-name medication-scheduling \
    --attribute-definitions \
        AttributeName=PK,AttributeType=S \
        AttributeName=SK,AttributeType=S \
    --key-schema \
        AttributeName=PK,KeyType=HASH \
        AttributeName=SK,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000 \
    --region us-east-1
```

#### Step 4: Start Service (Now Works)
```bash
# Service is configured to run on port 8083 by default
./gradlew run

# Test service is running
curl http://localhost:8083/
```

### Recent Fixes Applied ✅

#### DynamoDB Configuration Issues Resolved:
The service startup issues have been resolved by implementing service-specific DynamoDB qualifiers as required by the microservices architecture.

**Changes Made:**
- Added `@MedicationSchedulingDynamoDb` qualifier annotation for service-specific DynamoDB beans
- Updated DynamoDB configuration with `@Requires(notEnv = ["test"])` for production isolation
- Removed hardcoded credentials to match medication-catalog-service pattern
- Set default service port to 8083 to avoid conflicts

**Benefits:**
- Service can now run alongside other microservices without bean conflicts
- Follows CLAUDE.md microservices standards
- Clean startup without DynamoDB dependency injection issues
- Consistent configuration pattern across services

### Troubleshooting

1. **Port conflicts:**
   ```bash
   lsof -i :8083
   kill <PID>
   # Or use a different port: MICRONAUT_SERVER_PORT=8084 ./gradlew run
   ```

2. **Service startup issues (historical - now resolved):**
   - DynamoDB bean conflicts were resolved with service-specific qualifiers
   - If you encounter similar issues, ensure `@MedicationSchedulingDynamoDb` qualifier is used
   - Verify `@Requires(notEnv = ["test"])` is present on production beans

## Development Commands

### Known Working Commands
```bash
./gradlew test              # ✅ All tests pass
./gradlew build             # ✅ Build succeeds  
./gradlew run               # ✅ Starts successfully on port 8083
./gradlew clean build       # ✅ Clean build works
```

### Testing Commands
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