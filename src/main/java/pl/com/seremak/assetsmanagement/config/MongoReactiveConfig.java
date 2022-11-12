package pl.com.seremak.assetsmanagement.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Slf4j
@Configuration
public class MongoReactiveConfig {

    @Value("${spring.data.mongodb.database}")
    private String assetsManagementDatabase;

    @Value("${spring.data.mongodb.uri}")
    private String assetsManagementDatabaseUri;


    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient(), assetsManagementDatabase);
    }

    @Bean
    public MongoClient mongoClient() {
        log.info("Creating MongoDb client for URI: {}", assetsManagementDatabaseUri);
        return MongoClients.create(assetsManagementDatabaseUri);
    }
}