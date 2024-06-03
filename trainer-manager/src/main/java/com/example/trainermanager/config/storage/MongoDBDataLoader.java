package com.example.trainermanager.config.storage;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MongoDBDataLoader {

    private static final String SUCCESSFUL_MSG = "Successfully inserted {} docs";
    private static final String ERROR_MSG = "Error during insert (import) data. Exception message: {}";

    private static final String IMPORT_DOCS_WRAPPER = "docs";


    private final DefaultResourceLoader resourceLoader;

    public void loadData(MongoCollection<Document> collection, String filepath) {
        try {
            List<Document> docs = new ArrayList<>();

            Resource resource = resourceLoader.getResource(filepath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String data = br.lines().collect(Collectors.joining());
                docs.addAll(getDocs(data));
            } catch (IOException exception) {
                log.error(exception.getMessage());
                throw new RuntimeException(exception);
            }

            if (!docs.isEmpty()) {
                collection.insertMany(docs, new InsertManyOptions().ordered(false));
                log.info(SUCCESSFUL_MSG, docs.size());
            }
        } catch (MongoWriteException exception) {
            log.error(ERROR_MSG, exception.getMessage(), exception);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Document> getDocs(String data) {
        return Document.parse(data).get(IMPORT_DOCS_WRAPPER, ArrayList.class);
    }
}