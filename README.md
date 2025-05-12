# B2BM Claim Service

## Project Overview

The Claim Service is a critical component of the B2B Modernization project, responsible for managing the complete lifecycle of claims from initial import through verification and processing.

## Features
- TODO

## Technical Architecture

### Entity Structure
The project uses the following key entities:

- **ServiceOrder**: Represents a service order with tracking information
- **ServiceAttempt**: Records attempts made to service a particular order
- **ServiceOrderDataImportAuditInformation**: Tracks audit information for data imports

### Database Configuration
- JPA/Hibernate for ORM
- H2 in-memory database for development and testing
- Configured auto-auditing for entity creation and modification dates
- Database schema is managed by **Flyway**. See details below.

### Database Schema Management with Flyway

This project uses [Flyway](https://flywaydb.org/) to manage database schema migrations. This ensures that database schema changes are version-controlled, repeatable, and applied consistently across all environments.

-   **Migration Scripts Location**: SQL migration scripts are located in `src/main/resources/db/migration/`.
-   **Naming Convention**: Migration scripts must follow the Flyway naming convention:
    -   `V<VERSION>__<DESCRIPTION>.sql` (e.g., `V1__Initial_Schema.sql`, `V1.1__Add_new_column.sql`)
    -   The version number consists of one or more numeric parts, separated by a dot or an underscore.
-   **How it Works**: When the application starts, Flyway automatically checks the `flyway_schema_history` table in the database to see which migrations have already been applied. It then applies any new, pending migration scripts in version order.
-   **Creating New Migrations**:
    1.  Create a new SQL file in the `src/main/resources/db/migration/` directory.
    2.  Name it according to the convention, ensuring the version number is higher than the last applied migration.
    3.  Write the SQL DDL/DML statements for your schema change in this file.
-   **Local Development**: For the H2 in-memory database used during local development, Flyway will apply migrations each time the application starts, ensuring the schema is up-to-date.
-   **Flyway Maven Plugin**: The `flyway-maven-plugin` is configured in `pom.xml` and can be used for manual Flyway operations if needed (e.g., `mvn flyway:info`, `mvn flyway:migrate`, `mvn flyway:repair`).
    ```bash
    # Example: Get information about applied and pending migrations
    ./mvnw flyway:info
    ```

### Entity Auditing

All entities in the system extend a common `BaseEntity` abstract class that provides the following audit information:
- `createdDate`: When the entity was created
- `modifiedDate`: When the entity was last modified
- `createdBy`: Who created the entity
- `lastModifiedBy`: Who last modified the entity

The auditing is implemented using Spring Data JPA's auditing capabilities. Currently, the system uses a hardcoded value ("b2bm-claim-service") as the auditor for all entities until a proper authentication system is implemented.

This design follows the DRY (Don't Repeat Yourself) principle by centralizing the auditing logic in the `BaseEntity` class using `@MappedSuperclass`. It also leverages Lombok's `@SuperBuilder` pattern for clean entity creation.

When authentication is implemented in the future, the auditor information will be obtained from the security context without requiring changes to the entities themselves.

### External Dependencies

- **Database**: MySQL for persistent storage
- **Kafka**: Event messaging system for consuming service order events
- **File Storage**: For maintaining imported files

### Events Published

- **OrderCreated**: When a new order is successfully created

## Development Setup

### Prerequisites

- Java Development Kit (JDK) 21
- Maven 3.8+
- MySQL 8.0+
- Kafka

### Building the Project

```bash
# Clone the repository
git clone [repository-url]

# Navigate to project directory
cd b2bm-claim-service

# Build the project
./mvnw clean install
```

### Running Locally

```bash
# Run the application with Spring Boot
./mvnw spring-boot:run
```

### Accessing H2 Console
During development, you can access the H2 in-memory database at:
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:claimdb`
- Username: `sa`
- Password: (blank)

## Testing

```bash
# Run tests
./mvnw test
```

### Test Coverage

Test coverage is monitored using JaCoCo with a minimum coverage threshold of 90%.
Test coverage reports can be found in the `target/site/jacoco` directory after running tests:
```
b2bm-claim-service/target/site/jacoco/index.html
```

## Health Checks
Health checks are available at:
```
http://localhost:8080/actuator/health
```

## Development Tools

This project includes the following code quality tools:

### Linting Tools

1. **Checkstyle** - Enforces coding standards and conventions
    - Configuration: `src/main/resources/checkstyle.xml`
    - Run: `./mvnw checkstyle:check`

2. **SpotBugs** - Static analysis to look for bugs
    - Configuration: `src/main/resources/spotbugs-exclude.xml`
    - Run: `./mvnw spotbugs:check`

### Code Formatter

- **Google Java Format** - Automatic code formatter following Google's Java style guide
    - Run: `./mvnw fmt:format`

### Running All Quality Checks

To run all quality checks including linting and formatting:

```bash
./mvnw clean verify
```
or
```bash
sh ./scripts/lint.sh
```

## Logging

Logs are stored in the following locations:

- Application logs: `./logs/application.log`
- Archived logs: `./logs/archived/`

The logs directory is automatically created on application startup through the `./src/main/resources/logback-spring.xml` configuration.

More details about logging can be found in the `docs/logging.md` file.

## Contributing

Refer to `docs/CONTRIBUTING.md` for details.
