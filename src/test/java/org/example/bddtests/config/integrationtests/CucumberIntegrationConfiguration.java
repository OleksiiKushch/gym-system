package org.example.bddtests.config.integrationtests;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.example.bddtests.config.StaticBddTestsConfigProvider;
import org.example.bddtests.constants.BddTestsConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(
//        classes = {AppConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles({BddTestsConstants.PROFILE_TEST})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CucumberIntegrationConfiguration {

    private static final String ACTIVEMQ_NETWORK_ALIAS = "activemq";
    private static final String MONGO_NETWORK_ALIAS = "trainer-manager-db";

    private static MySQLContainer<?> mySqlContainer;
    private static ActiveMQContainer activeMQContainer;
    private static MongoDBContainer mongoDBContainer;
    private static GenericContainer<?> trainerManagerContainer;

    @BeforeAll
    @SuppressWarnings("resource")
    public static void setUp() { // or in static init block
        StaticBddTestsConfigProvider.Config config = StaticBddTestsConfigProvider.getConfigs();

        Network network = Network.newNetwork();
        activeMQContainer = new ActiveMQContainer(config.getMessageBroker().getDockerImage())
                .withNetwork(network)
                .withNetworkAliases(ACTIVEMQ_NETWORK_ALIAS);
        mongoDBContainer = new MongoDBContainer(config.getTrainerManager().getDatabaseDockerImage())
                .withNetwork(network)
                .withNetworkAliases(MONGO_NETWORK_ALIAS);
        trainerManagerContainer = new GenericContainer<>(createImageByDockerfile(config.getTrainerManager().getPathToDockerfile(),
                        Map.of(BddTestsConstants.SKIP_TESTS_DOCKER_ARG, Boolean.TRUE.toString())))
                .withNetwork(network)
                .withEnv(Map.of(
                        BddTestsConstants.SPRING_PROFILES_ACTIVE_PROPERTY, BddTestsConstants.PROFILE_TEST,
                        BddTestsConstants.MONGODB_URI_PROPERTY, formMongoUrl(),
                        BddTestsConstants.ACTIVEMQ_BROKER_URL_PROPERTY, formActivemqUrl()
                ));
        mySqlContainer = new MySQLContainer<>(config.getDatabase().getDockerImage()).withDatabaseName(config.getDatabase().getName());


        activeMQContainer.start();

        mongoDBContainer.start();
        trainerManagerContainer.start();

        mySqlContainer.start();
    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add(BddTestsConstants.DATASOURCE_URL_KEY, mySqlContainer::getJdbcUrl);
        propertyRegistry.add(BddTestsConstants.DATASOURCE_USERNAME_KEY, mySqlContainer::getUsername);
        propertyRegistry.add(BddTestsConstants.DATASOURCE_PASSWORD_KEY, mySqlContainer::getPassword);

        propertyRegistry.add(BddTestsConstants.ACTIVEMQ_BROKER_URL_KEY, activeMQContainer::getBrokerUrl);
        propertyRegistry.add(BddTestsConstants.ACTIVEMQ_USER_KEY, activeMQContainer::getUser);
        propertyRegistry.add(BddTestsConstants.ACTIVEMQ_PASSWROD_KEY, activeMQContainer::getPassword);
    }

    @AfterAll
    public static void tearDown() { // or static/non-static method with @PreDestroy
        mySqlContainer.stop();

        trainerManagerContainer.stop();
        mongoDBContainer.stop();

        activeMQContainer.stop();
    }

    private static ImageFromDockerfile createImageByDockerfile(String pathToDockerfile, Map<String, String> buildArgs) {
        return new ImageFromDockerfile()
                .withDockerfile(Paths.get(pathToDockerfile))
                .withBuildArgs(buildArgs);
    }

    private static String formMongoUrl() {
        return BddTestsConstants.MONGO_PROTOCOL + MONGO_NETWORK_ALIAS + BddTestsConstants.COLON + BddTestsConstants.DEFAULT_MONGO_PORT +
                BddTestsConstants.SLASH + BddTestsConstants.TRAINER_MANAGER_DB;
    }

    private static String formActivemqUrl() {
        return BddTestsConstants.ACTIVEMQ_PROTOCOL + ACTIVEMQ_NETWORK_ALIAS + BddTestsConstants.COLON + BddTestsConstants.DEFAULT_ACTIVEMQ_PORT;
    }
}
