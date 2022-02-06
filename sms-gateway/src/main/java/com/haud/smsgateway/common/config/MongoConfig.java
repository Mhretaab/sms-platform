package com.haud.smsgateway.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.haud.smsgateway"})
public class MongoConfig extends AbstractReactiveMongoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

	@Value("${db.url}")
	private String dburl;

	@Value("${db.port}")
	private String dbport;

	@Value("${db.name}")
	private String dbName;

	@Override
	public MongoClient reactiveMongoClient() {
		logger.info("Creating mongo client");
		return MongoClients.create(new ConnectionString(String.format("mongodb://%s:%s", dburl, dbport)));
	}

	@Override
	protected String getDatabaseName() {
		return dbName;
	}

	@Override
	public boolean autoIndexCreation() {
		return true;
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		logger.info("Creating reactive mongo template");
		return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
	}
}