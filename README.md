# Gym System

**EPAM Internal X-Stack Mentoring Program** \**02/01/2024 – 07/01/2024*A g## Project Description

---

## Project Description

---

## Achievements

Completed all tasks (see full description of tasks [here](course-tasks.zip)), including some interesting ones such as:

- Implemented, for the Login endpoint, a configurable anti-brute-force protection system using Guava `LoadingCache`;
- Implemented custom AOP aspects for:
    - Transaction Logging: a UUID-based transaction ID via SLF4J MDC for every `@Transactional` method across service boundaries, even between microservices;
    - Circuit Breaker: which monitors inter-service failures and blocks cascading calls.

---

## Key Technologies

- **Core:** Java 17, Spring (Core, Boot, MVC, Security (JWT), Data, Cloud, AOP, Validation, Actuator, Config, SpringDoc OpenAPI)
- **Persistence:** JPA / Hibernate, MySQL, MongoDB, Liquibase
- **Testing:** JUnit, Mockito, Cucumber (Gherkin), Testcontainers, JaCoCo
- **Monitoring:** Micrometer, Prometheus, Grafana, Logback
- **Microservices:** Netflix Eureka, JMS (ActiveMQ), OpenFeign
- **Libraries:** Lombok, ModelMapper, Apache Commons Lang, Jackson, Guava
- **Others:** Maven, Docker (Compose), Swagger, AWS (IAM, S3)

---

## Project Structure

```
gym-system/
├── src/                          # Main REST API (Spring Boot)
├── Dockerfile                    # gym-system container image
├── trainer-manager/              # Microservice: trainer workload (MongoDB)
├── config-server/                # Microservice: centralized configuration (Spring Cloud Config)
├── eureka-server/                # Microservice: service discovery (Netflix Eureka)
├── docker-compose.yaml           # Full stack orchestration
├── monitoring/                   # Prometheus + Grafana configs
├── aws/                          # AWS IAM policies, budgets, CloudFormation
├── for-test/test-cors/           # CORS testing application
├── my-git-task.zip               # Git task completion archive
└── course-tasks.zip              # Original task descriptions archive
```

---

## Testing

### Cucumber Documentation

- [Cucumber Official Documentation](https://cucumber.io/docs)
- [Cucumber + Spring Documentation](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-spring)

### Run All Cucumber Tests

#### Component Tests

```bash
mvn test -Dtest=org.example.bddtests.RunCucumberComponentTests
```

#### Integration Tests

```bash
mvn test -Dtest=org.example.bddtests.RunCucumberIntegrationTests
```

### Run Specific Cucumber Tests by Tag

Use the `cucumber.filter.tags` property to execute specific scenarios or feature groups.

Example:

```bash
mvn test \
  -Dtest=org.example.bddtests.RunCucumberComponentTests \
  -Dcucumber.filter.tags="@TraineeRegistration"
```

### Combining Tags

You can combine tags using logical operators:

#### AND

```bash
-Dcucumber.filter.tags="@Tag1 and @Tag2"
```

#### OR

```bash
-Dcucumber.filter.tags="@Tag1 or @Tag2"
```

#### NOT

```bash
-Dcucumber.filter.tags="not @Tag"
```

### Example

```bash
mvn test \
  -Dtest=org.example.bddtests.RunCucumberComponentTests \
  -Dcucumber.filter.tags="@Registration and not @Negative"
```
