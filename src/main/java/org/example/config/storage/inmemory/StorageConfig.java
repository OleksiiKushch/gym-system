package org.example.config.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.entity.CsvRecordInitializer;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class StorageConfig {

    private static final String TRAINEE_STORAGE_BEAN_NAME = "traineeStorage";
    private static final String TRAINER_STORAGE_BEAN_NAME = "trainerStorage";
    private static final String TRAINING_STORAGE_BEAN_NAME = "trainingStorage";

    @Value("${testdata.filepath.trainee}")
    private String traineeTestDataFilePath;

    @Value("${testdata.filepath.trainer}")
    private String trainerTestDataFilePath;

    @Value("${testdata.filepath.training}")
    private String trainingTestDataFilePath;

    @Bean
    public InMemoryStorageWithIntId<Trainee> traineeStorage() {
        return new InMemoryStorageWithIntId<>();
    }

    @Bean
    public InMemoryStorageWithIntId<Trainer> trainerStorage() {
        return new InMemoryStorageWithIntId<>();
    }

    @Bean
    public InMemoryStorage<String, Training> trainingStorage() {
        return new InMemoryStorage<>();
    }

    @Bean
    public BeanPostProcessor storagesPostProcessor() {
        return new BeanPostProcessor() {
            @SuppressWarnings("unchecked")
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
                if (bean instanceof InMemoryStorage) {
                    try {
                        switch (beanName) {
                            case TRAINEE_STORAGE_BEAN_NAME -> processRecords(createCSVParser(traineeTestDataFilePath),
                                            (InMemoryStorage<Integer, Trainee>) bean, Trainee::getUserId, Trainee::new);
                            case TRAINER_STORAGE_BEAN_NAME -> processRecords(createCSVParser(trainerTestDataFilePath),
                                    (InMemoryStorage<Integer, Trainer>) bean, Trainer::getUserId, Trainer::new);
                            case TRAINING_STORAGE_BEAN_NAME -> processRecords(createCSVParser(trainingTestDataFilePath),
                                            (InMemoryStorage<String, Training>) bean, Training::getTrainingName, Training::new);
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
                return bean;
            }
        };
    }

    private CSVParser createCSVParser(String filePath) throws IOException {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader().setSkipHeaderRecord(true)
                .build();
        return new CSVParser(new FileReader(filePath), format);
    }

    private <K, V extends CsvRecordInitializer> void processRecords(CSVParser parser, InMemoryStorage<K, V> storage,
                                                                    Function<V, K> keyExtractor,
                                                                    Supplier<V> instanceSupplier) {
        for (CSVRecord record : parser) {
            V object = instanceSupplier.get();
            object.initByCsvRecord(record);
            K key = keyExtractor.apply(object);
            storage.put(key, object);
        }
    }
}
