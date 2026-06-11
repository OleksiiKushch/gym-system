# Gym System

**EPAM Internal X-Stack Mentoring Program**
**02/01/2024 – 07/01/2024**

## Project Description

---

## Achievements

---

## Key Technologies

---

## Technologies

The following technologies have been used across the project lifecycle (including those later replaced during development):

### Languages & Platforms
| Technology | Version | Status |
|---|---|---|
| Java | 17 | Current |
| YAML | — | Current |
| HTML / JavaScript | — | Current (CORS test app) |
| SQL | — | Current (Liquibase) |
| JSON / CSV | — | Current |

### Core Frameworks
| Technology | Version | Status |
|---|---|---|
| Spring Boot | 3.2.4 | Current |
| Spring Framework | 6.1.x | Replaced by Spring Boot |
| Spring Cloud | 2023.0.1 | Current |
| Spring MVC | — | Current |
| Spring Web | — | Current |
| Spring Data JPA / Hibernate | — | Current |
| Spring Data MongoDB | — | Current |
| Spring Security | — | Current |
| Spring Validation (Jakarta) | — | Current |
| Spring AOP (AspectJ) | — | Current |
| Spring Scheduling | — | Current |
| Spring JMS | — | Current |

### Microservices & Cloud
| Technology | Version | Status |
|---|---|---|
| Netflix Eureka (Service Discovery) | 2023.0.1 | Current |
| Spring Cloud Config Server | 2023.0.1 | Current |
| OpenFeign (Declarative REST Client) | 2023.0.1 | Current |
| Monolithic Architecture | — | Replaced by microservices |

### Databases & Persistence
| Technology | Version | Status |
|---|---|---|
| MySQL | 8.3.0 | Current |
| MongoDB | — | Current (trainer-manager) |
| Hibernate ORM (manual config) | 6.4.4 | Replaced by Spring Boot Data JPA |
| In-memory storage (CSV files) | — | Replaced by JPA |
| Liquibase | — | Current |
| HikariCP | — | Current |
| MySQL Connector/J | 8.3.0 | Current |

### Messaging
| Technology | Version | Status |
|---|---|---|
| ActiveMQ | — | Current |
| Spring JMS | — | Current |
| Jackson JMS Converter | — | Current |
| Synchronous Feign calls | — | Supplemented by async JMS |

### Security
| Technology | Version | Status |
|---|---|---|
| Spring Security | — | Current |
| JWT (jjwt) | 0.12.5 | Current |
| BCrypt | — | Current |
| Custom Brute Force Prevention | — | Current |
| CORS configuration | — | Current |
| ControllerAuthorizingAspect (custom AOP auth) | — | Replaced by Spring Security |
| AuthenticateTokenService | — | Replaced by JWT |

### API Documentation
| Technology | Version | Status |
|---|---|---|
| Springdoc OpenAPI | 2.3.0 | Current |
| Swagger UI | — | Current |

### Object Mapping
| Technology | Version | Status |
|---|---|---|
| ModelMapper | 3.2.0 | Current |
| Jackson (incl. JSR310) | — | Current |
| Custom Mapper interface | — | Replaced by ModelMapper |

### Monitoring & Observability
| Technology | Version | Status |
|---|---|---|
| Micrometer + Prometheus | — | Current |
| Grafana | — | Current |
| Spring Boot Actuator | — | Current |
| Logback (SLF4J) | — | Current |
| Custom Circuit Breaker (AOP) | — | Current |

### Utilities
| Technology | Version | Status |
|---|---|---|
| Lombok | — | Current |
| Guava | 33.1.0-jre | Current |
| SnakeYAML | 2.2 | Current |
| Apache Commons Lang3 | — | Current |
| Apache Commons CSV | 1.10.0 | Replaced by JPA |

### Testing
| Technology | Version | Status |
|---|---|---|
| JUnit 5 (Jupiter) | — | Current |
| JUnit Vintage Engine | 5.11.0-M1 | Current |
| Cucumber (BDD) | 7.17.0 | Current |
| Testcontainers | 1.19.8 | Current |
| JaCoCo (Code Coverage) | 0.8.12 | Current |
| Maven Surefire Plugin | 3.2.5 | Current |
| Mockito | — | Current |
| Spring Boot Test | — | Current |

### Build & Containerization
| Technology | Version | Status |
|---|---|---|
| Apache Maven | — | Current |
| Spring Boot Maven Plugin | — | Current |
| Docker (multi-stage builds) | — | Current |
| Docker Compose | 3.8 | Current |
| Eclipse Temurin JRE | 17-jre-alpine | Current |
| Cargo Maven Plugin (Tomcat 10) | — | Replaced by Docker |
| WAR packaging | — | Replaced by JAR |

### Cloud (AWS)
| Technology | Status |
|---|---|
| AWS IAM Policies | Removed (during cleanup) |
| AWS S3 Bucket Policies | Removed (during cleanup) |
| AWS Organizations Policy | Removed (during cleanup) |
| AWS Budgets | Removed (during cleanup) |

### IDE & Project Config
| Technology | Status |
|---|---|
| IntelliJ IDEA (.idea, .iml) | Current |
| .gitignore | Current |

---

## Tasks

Description of Tasks: [here](course-tasks.zip)

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
