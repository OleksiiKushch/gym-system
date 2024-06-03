package org.example.bddtests.config.componenttests;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.example.bddtests.config.StaticBddTestsConfigProvider;
import org.example.bddtests.constants.BddTestsConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@CucumberContextConfiguration
@SpringBootTest(
//        classes = {AppConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles({BddTestsConstants.PROFILE_TEST})
public class CucumberSpringConfiguration {

    @Getter @Setter
    private static String databaseDockerImage;
    @Getter @Setter
    private static String databaseName;

    static {
        StaticBddTestsConfigProvider.Config config = StaticBddTestsConfigProvider.getConfigs();
        setDatabaseDockerImage(config.getDatabase().getDockerImage());
        setDatabaseName(config.getDatabase().getName());
    }

    private static MySQLContainer<?> container;

    @BeforeAll
    @SuppressWarnings("resource")
    public static void setUp() { // or in static init block
        container = new MySQLContainer<>(getDatabaseDockerImage()).withDatabaseName(getDatabaseName());
        container.start();
    }

//    @TestConfiguration
//    static class PostgresTestConfiguration {
//
//        @Bean
//        DataSource dataSource() {
//            HikariConfig hikariConfig = new HikariConfig();
//            hikariConfig.setJdbcUrl(container.getJdbcUrl());
//            hikariConfig.setUsername(container.getUsername());
//            hikariConfig.setPassword(container.getPassword());
//            return new HikariDataSource(hikariConfig);
//        }
//    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add(BddTestsConstants.DATASOURCE_URL_KEY, container::getJdbcUrl);
        propertyRegistry.add(BddTestsConstants.DATASOURCE_USERNAME_KEY, container::getUsername);
        propertyRegistry.add(BddTestsConstants.DATASOURCE_PASSWORD_KEY, container::getPassword);
    }

    @AfterAll
    public static void tearDown() { // or static/non-static method with @PreDestroy
        container.stop();
    }
}
