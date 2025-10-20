# SerenityBDD Test Automation Framework

## Overview

This framework implements automated testing for:
- UI Testing: [Sauce Demo](https://www.saucedemo.com/) e-commerce website using Serenity BDD with Selenium WebDriver
- API Testing: [Postcodes.io](https://api.postcodes.io/docs) service using Serenity BDD with REST Assured

## Prerequisites

- JDK 11 or higher
- Maven 3.8.4 or higher
- Chrome/Firefox browser for local testing
- Docker and Docker Compose (for remote execution)

## Test Execution

### Local Execution

Run all tests:
```bash
mvn clean verify
```

Run specific test types:
```bash
# UI tests only
mvn clean verify -Dcucumber.filter.tags="@UI"

# API tests only
mvn clean verify -Dcucumber.filter.tags="@API"

# Smoke tests
mvn clean verify -Dcucumber.filter.tags="@Smoke"

# Regression tests
mvn clean verify -Dcucumber.filter.tags="@Regression"
```

### Remote Execution (Docker)

1. Start Selenium Grid:
```bash
docker-compose up -d --build
```

2. Run tests:
```bash
# All tests
mvn clean verify -Denvironment=remote

# UI tests only
mvn clean verify -Denvironment=remote -Dcucumber.filter.tags="@UI"

# API tests only
mvn clean verify -Denvironment=remote -Dcucumber.filter.tags="@API"
```

3. Stop Selenium Grid:
```bash
docker-compose down
```

## Test Reports

Serenity generates HTML reports after test execution:

Location: `target/site/serenity/index.html`


## Test Categories (Tags)

- `@UI`: UI tests for Sauce Demo
- `@API`: API tests for Postcodes.io
- `@Smoke`: Critical path tests
- `@Regression`: Full regression suite

## Configuration

### Local Execution Defaults
- Default browser: Chrome
- Headless mode: Enabled
- Screenshots: On failure
- Parallel execution: Enabled

### Remote Execution
- Selenium Grid enabled
- Chrome containers
- Parallel execution supported
- Screenshots captured within containers

## Troubleshooting

### Common Issues

1. Local Execution
    - Verify browser driver versions
    - Check browser compatibility
    - Validate test user credentials

2. Remote Execution
    - Ensure Docker is running
    - Verify container health
    - Check network connectivity
    - Validate Selenium Grid status

3. Report Generation
    - Clear target directory: `mvn clean`
    - Verify disk space
    - Check file permissions

### Debugging Tips

- Enable debug logs in serenity.conf
- Check container logs:
  ```bash
  docker-compose logs selenium-hub
  docker-compose logs chrome
  ```
- Verify environment variables
- Review Serenity reports for failure details
