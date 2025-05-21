# Contributing to B2BM Service Order Service

Thank you for your interest in contributing to the B2BM Service Order Service! This document provides guidelines and instructions for contributing to this project.

## Table of Contents

- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Git Workflow](#git-workflow)
- [Pull Request Process](#pull-request-process)
- [Testing Guidelines](#testing-guidelines)
- [Documentation](#documentation)

## Development Setup

### Prerequisites

- Java Development Kit (JDK) 21
- Maven 3.8+
- MySQL 8.0+
- Kafka
- Git

### Getting Started

1. Fork the repository
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/b2bm-claim-service.git
   cd b2bm-claim-service
   ```
3. Set up the upstream remote:
   ```bash
   git remote add upstream https://github.com/ORIGINAL-OWNER/b2bm-claim-service.git
   ```
4. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Coding Standards

We follow specific conventions for different components in our codebase:

### Java Code Style

- Use 4 spaces for indentation (not tabs)
- Follow Java naming conventions:
  - Classes: CamelCase (e.g., `ServiceOrder`)
  - Methods/Variables: camelCase (e.g., `verifyOrder()`)
  - Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)
- Maximum line length: 120 characters
- Include Javadoc comments for public methods and classes

### Service Classes
- Create service interfaces for all service implementations
- Use constructor-based dependency injection
- Implement `@Transactional` at service layer for database operations
- Use DTOs for data transfer between layers, not entity objects

### Entity Classes
- Annotate entity classes with `@Entity`
- Use Lombok annotations appropriately
- Annotate entity ID with `@Id` and `@GeneratedValue`
- Use FetchType.LAZY for relationships unless specified otherwise

### Controllers
- Annotate controller classes with `@RestController` and appropriate `@RequestMapping`
- Return `ResponseEntity<T>` with appropriate HTTP status codes
- Use `@Valid` annotation to validate request DTOs
- Document APIs with OpenAPI annotations

## Git Workflow

1. Keep your branch updated with upstream:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```
2. Make small, focused commits with clear messages
3. Commit messages should:
   - Start with a verb in imperative mood (e.g., "Add", "Fix", "Update")
   - First line is a summary (50 chars max)
   - Optionally followed by a blank line and detailed description

## Pull Request Process

1. Ensure your code follows our coding standards
2. Update documentation as necessary
3. Make sure all tests pass
4. Submit the PR to the `main` branch
5. PR title should follow the format: `[TYPE]: Brief description`
   - Types: `FEAT`, `FIX`, `DOCS`, `STYLE`, `REFACTOR`, `TEST`, `CHORE`
6. Fill out the PR template with:
   - Description of changes
   - Issue number(s) addressed
   - Testing performed
   - Screenshots (if UI changes)

## Testing Guidelines

- Write unit tests for all new code
- Maintain a minimum test coverage of 80%
- Tests should be independent and repeatable
- Follow naming pattern: `methodName_stateUnderTest_expectedBehavior`
- Use appropriate test slicing with specialized annotations:
  - `@WebMvcTest` for controllers
  - `@JdbcTest` for repositories
  - `@SpringBootTest` for integration tests

## Documentation

- Update README.md if you add or change functionality
- Document public APIs with Javadoc
- Add or update comments for complex sections of code
- Keep API documentation up to date with Spring RestDocs

Thank you for contributing to the B2BM Service Order Service! 