package com.example.trainermanager.config.storage.test;

import com.example.trainermanager.config.storage.MongoDBDataLoader;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Profile("test")
@Component
public class LoadTestData {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.collections.report}")
    private String reportsCollectionName;
    @Value("${spring.data.mongodb.collections.report.test-data-filepath}")
    private String testReportsFilePath;

    private final MongoDBDataLoader dataLoader;

    @PostConstruct
    public void loadTestData() {
        MongoClient client = MongoClients.create(getMongoUri());
        MongoDatabase database = client.getDatabase(getDatabaseName());
        MongoCollection<Document> collection = database.getCollection(getReportsCollectionName());
        dataLoader.loadData(collection, getTestReportsFilePath());
    }

    private String getDatabaseName() {
        return new ConnectionString(getMongoUri()).getDatabase();
    }
}
