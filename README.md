# Gym System

**EPAM Internal X-Stack Mentoring Program** _02/01/2024 - 07/01/2024_

## Project Description

Gym System is a microservices-based training management platform built with Java and Spring Boot. It provides role-based access for trainees and trainers, supporting registration, profile management, training session scheduling, and trainer workload tracking. The system implements fault tolerance patterns including a Circuit Breaker, alongside a Dead Letter Queue (DLQ) for failed JMS messages to ensure reliable asynchronous communication via ActiveMQ. It uses MySQL for primary persistence with MongoDB for trainer workload storage, JWT-based authentication, and service discovery (Eureka) with centralized configuration (Spring Cloud Config). The API is fully documented via Swagger/OpenAPI, with comprehensive monitoring through Prometheus and Grafana, and automated testing with Cucumber. The entire stack is containerized with Docker Compose for seamless local development and deployment.

## Achievements

Completed all tasks (see full description of tasks [here](course-tasks.zip)), including some interesting ones such as:

- Implemented, for the Login endpoint, a configurable anti-brute-force protection system using Guava `LoadingCache`;
- Implemented custom AOP aspects for:
    - Transaction Logging: a UUID-based transaction ID via SLF4J MDC for every `@Transactional` method across service boundaries, even between microservices;
    - Circuit Breaker: which monitors inter-service failures and blocks cascading calls.

## Key Technologies

- **Core:** Java 17, Spring (Core, Boot, MVC, Security (JWT), Data, Cloud, AOP, Validation, Actuator, Config, SpringDoc OpenAPI)
- **Persistence:** JPA / Hibernate, MySQL, MongoDB, Liquibase
- **Testing:** JUnit, Mockito, Cucumber (Gherkin), Testcontainers, JaCoCo
- **Monitoring:** Micrometer, Prometheus, Grafana, Logback
- **Microservices:** Netflix Eureka, JMS (ActiveMQ), OpenFeign
- **Libraries:** Lombok, ModelMapper, Apache Commons Lang, Jackson, Guava
- **Others:** Maven, Docker (Compose), Swagger, AWS (IAM, S3)

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

## Prerequisites

Before running the project, ensure you have the following installed:

| Requirement                 | Version              |
| --------------------------- | -------------------- |
| **Java**                    | 17 (OpenJDK 17)      |
| **Maven**                   | 3.8.5+               |
| **Docker & Docker Compose** | Docker Compose v3.8+ |

The following services are automatically provisioned via Docker:

- **MySQL 8.3.0**: main persistence
- **MongoDB latest**: trainer workload storage
- **ActiveMQ**: JMS message broker

## How to Run

### Docker Compose (full stack)

```bash
docker-compose up --build
```

### Ports Summary

| Port  | Service               | Notes                                              |
| ----- | --------------------- | -------------------------------------------------- |
| 8080  | gym-system            | Main REST API, Swagger UI at `/swagger-ui.html`    |
| 5005  | gym-system            | JDWP debug                                         |
| 8081  | trainer-manager       | Trainer workload microservice                      |
| 8761  | eureka-server         | Eureka Dashboard (service discovery)               |
| 8888  | config-server         | Spring Cloud Config Server (centralized config)    |
| 8161  | activemq              | Admin Console (`admin` / `admin`)                  |
| 61616 | activemq              | JMS broker TCP port                                |
| 27017 | trainer-manager-mongo | MongoDB                                            |
| 3308  | db (MySQL)            | Database `gymsystem`, user `root`, password `toor` |
| 3000  | grafana               | (optional) Monitoring dashboard                    |
| 9090  | prometheus            | (optional) Metrics collection                      |

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
