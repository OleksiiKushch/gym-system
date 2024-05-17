package org.example.bddtests.config;

import lombok.Data;
import org.example.bddtests.config.componenttests.CucumberSpringConfiguration;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class StaticBddTestsConfigProvider {

    private static final String CONFIG_FILE_PATH = "bddtests/config.yaml";

    public static Config getConfigs() {
        Yaml yaml = new Yaml(new Constructor(Config.class, new LoaderOptions()));
        InputStream inputStream = CucumberSpringConfiguration.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE_PATH);
        return yaml.load(inputStream);
    }

    @Data
    public static class Config {

        private Database database;
        private MessageBroker messageBroker;
        private TrainerManager trainerManager;

        @Data
        public static class Database {

            private String dockerImage;
            private String name;
        }

        @Data
        public static class MessageBroker {

            private String dockerImage;
        }

        @Data
        public static class TrainerManager {

            private String pathToDockerfile;
            private String databaseDockerImage;
        }
    }
}
