# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Vaadin/jOOQ template project that demonstrates integration of Vaadin Flow with jOOQ for type-safe database access, using Spring Boot as the foundation. The project uses Spring Security with JWT authentication and includes comprehensive testing setups with both Karibu Testing and Playwright.

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.5.5, jOOQ 3.20.6, PostgreSQL
- **Frontend**: Vaadin 24.8.3 (Flow), custom CSS themes
- **Database**: PostgreSQL with Flyway migrations
- **Security**: Spring Security with JWT authentication  
- **Testing**: JUnit 5, Karibu Testing (browser-less UI tests), Playwright (E2E tests), TestContainers
- **Code Quality**: ErrorProne, NullAway, Spring Java Format, SonarCloud, JaCoCo

## Development Commands

### Building and Running
```bash
# Generate jOOQ metamodel (required before first run)
./mvnw compile

# Run application in development mode (requires Docker for TestContainers)
./mvnw spring-boot:run

# Build production JAR
./mvnw clean package -Pproduction

# Run production build
java -jar target/vaadin-jooq-template-<version>.jar
```

### Testing
```bash
# Run all tests (unit + integration)
./mvnw verify

# Run only unit tests
./mvnw test

# Run only integration tests
./mvnw integration-test

# Run with coverage
./mvnw verify -Pcoverage

# Run SonarCloud analysis
./mvnw verify sonar:sonar -Pcoverage
```

### Code Quality
```bash
# Format code (Spring Java Format)
./mvnw spring-javaformat:apply

# Validate formatting
./mvnw spring-javaformat:validate
```

## Architecture

### Package Structure
```
ch.martinelli.vj/
├── core/                      # Core application components
│   ├── configuration/         # Spring configurations
│   ├── domain/               # User management, security domain
│   ├── security/             # Security configuration, JWT handling
│   └── ui/                   # Core UI components and layouts
├── person/                   # Person domain (example feature)
│   ├── domain/              # PersonDAO, domain logic
│   └── ui/                  # Person views and forms
└── greeting/                # Greeting domain (example feature)
    └── ui/                  # Hello World view
```

### Database Layer (jOOQ)
- **Code Generation**: Uses `testcontainers-jooq-codegen-maven-plugin` with PostgreSQL TestContainer
- **Migrations**: Flyway migrations in `src/main/resources/db/migration/`
- **Configuration**: `VjJooqConfiguration` enables optimistic locking
- **Generated Classes**: Located in `ch.martinelli.vj.db` package (auto-generated)

### Security Architecture
- **JWT Authentication**: Stateless JWT tokens for better developer experience
- **User Management**: `UserDAO`, `UserDetailsServiceImpl` for Spring Security integration
- **Authorization**: Role-based access using `@RolesAllowed` annotations
- **Test Security**: `KaribuTest` provides login helpers for testing

### UI Architecture
- **Layout**: `MainLayout` with Vaadin App Layout, side navigation, and user menu
- **Views**: Each feature has its own view package with Vaadin Flow components
- **I18N**: Support for English and German with `TranslationProvider`
- **Testing**: Two-tiered testing approach:
  - `KaribuTest`: Fast browser-less testing with Karibu Testing
  - `PlaywrightIT`: Full E2E testing with Playwright

## Important Development Notes

### Running the Application
- **Docker Required**: TestContainers needs Docker running for database
- **Development Class**: Use `TestVjApplication` for development (auto-starts PostgreSQL container)
- **jOOQ Generation**: Always run `./mvnw compile` after database schema changes

### Database Development
- **Schema Changes**: Create new Flyway migration files in `src/main/resources/db/migration/`
- **Naming**: Use `V###__description.sql` format for migration files
- **Regeneration**: Run `./mvnw compile` to regenerate jOOQ classes after schema changes

### Testing Strategy
- **Unit Tests**: Extend `KaribuTest` for fast UI testing without browser
- **Integration Tests**: Extend `PlaywrightIT` for full E2E browser testing
- **Database**: Both test base classes use TestContainers for isolated database testing
- **Mopo**: Playwright tests use Mopo library for simplified Vaadin component interaction

### Code Quality Standards
- **ErrorProne**: Enabled with NullAway for null safety
- **Formatting**: Spring Java Format enforced (fails build on violations)
- **Coverage**: JaCoCo integration with SonarCloud analysis
- **Annotations**: Use `@Nullable` from JSpecify for null safety

### Security Configuration
- **JWT Secret**: Change `jwt.auth.secret` in production (use `openssl rand -base64 32`)
- **Development**: Default credentials and setup suitable for development only
- **Authentication Flow**: JWT tokens stored client-side, validated server-side

## Profiles and Build Variants

- **Default**: Development mode with live reload
- **production**: Optimized build with frontend compilation (`-Pproduction`)
- **coverage**: Enables JaCoCo coverage reports (`-Pcoverage`)