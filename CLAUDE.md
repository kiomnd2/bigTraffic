# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**big-traffic** is a Spring Boot 3.5.8 web application built with Java 21 and Gradle. The project appears to be in early stages with minimal application code currently implemented.

## Build and Development Commands

### Building the Project
```bash
# Build the project
./gradlew build

# Build without running tests
./gradlew build -x test

# Clean build
./gradlew clean build
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "kr.kiomn2.bigtraffic.BigTrafficApplicationTests"

# Run tests with detailed output
./gradlew test --info
```

### Running the Application
```bash
# Run the Spring Boot application
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Docker Environment
```bash
# Start MySQL database
docker-compose -f docker/docker-compose.yml up -d

# Stop MySQL database
docker-compose -f docker/docker-compose.yml down

# View logs
docker-compose -f docker/docker-compose.yml logs -f
```

## Architecture and Structure

### Package Structure
- **Base Package**: `kr.kiomn2` (note: package structure has been recently refactored from `kr.kiomn2.newsletter.bigtraffic`)
- **Main Application**: `BigTrafficApplication.java` in `src/main/java/kr/kiomn2/`
- **Tests**: Located in `src/test/java/kr/kiomn2/bigtraffic/` (note inconsistency with main package)

### Technology Stack
- **Framework**: Spring Boot 3.5.8
- **Language**: Java 21
- **Build Tool**: Gradle with Kotlin DSL
- **Web**: Spring Web (REST APIs)
- **Utilities**: Lombok for boilerplate reduction
- **Database**: MySQL 5.7 (via Docker)
- **Testing**: JUnit Platform with Spring Boot Test

### Database Configuration
The project uses MySQL 5.7 via Docker Compose:
- **Host**: localhost:3306
- **Database**: kiomnd2-db
- **User**: kiomnd2
- **Character Set**: utf8mb4 with utf8mb4_unicode_ci collation

Database credentials are defined in `docker/docker-compose.yml`. Application database configuration should be added to `application.properties`.

### Recent Package Refactoring
The codebase has undergone a package restructuring:
- Main application moved from `kr.kiomn2.newsletter.bigtraffic` to `kr.kiomn2.bigtraffic`
- Test package path still references old structure (`kr.kiomn2.bigtraffic`)
- This inconsistency should be noted when creating new packages or moving files

## Development Notes

### Windows Environment
This project is developed on Windows (`win32`). Use `gradlew.bat` instead of `./gradlew` when running Gradle commands directly in Windows Command Prompt (though `./gradlew` works in Git Bash/WSL).

### Gradle Configuration
- Group ID: `kr.kiomn2.bigtraffic`
- Version: `0.0.1`
- Java Toolchain: Automatically downloads/uses Java 21
