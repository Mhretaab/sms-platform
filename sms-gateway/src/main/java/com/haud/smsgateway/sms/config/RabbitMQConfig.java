package com.haud.smsgateway.sms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

	public static final String TOPIC_EXCHANGE_NAME = "sms-exchange";
	public static final String SPAM_FILTER_QUEUE = "spam-filter-queue";
	public static final String SPAM_FILTER_BINDING_KEY = "spam-filter-key";
	public static final String CHARGING_MODULE_QUEUE = "charging-module-queue";
	public static final String CHARGING_MODULE_BINDING_KEY = "charging-module-key";

	@Value("${mq.host}")
	private String mqHost;
	@Value("${mq.port}")
	private int mqPort;
	@Value("${mq.username}")
	private String mqUsername;
	@Value("${mq.password}")
	private String mqPasword;

	//TODO: MAKE CONCURRENT CONSUMERS CONFIGURABLE
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitMQConnectionFactory());
		factory.setMessageConverter(producerJackson2MessageConverter());
		factory.setConcurrentConsumers(16);
		factory.setMaxConcurrentConsumers(16);
		return factory;
	}

	@Bean
	public ConnectionFactory rabbitMQConnectionFactory() {
		logger.info("rabbitMQConnectionFactory() : Establishing rabbitmq connection with given configuration");

		final com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();

		factory.setHost(mqHost);
		factory.setPort(mqPort);
		factory.setUsername(mqUsername);
		factory.setPassword(mqPasword);
		factory.setAutomaticRecoveryEnabled(true);

		return new CachingConnectionFactory(factory);
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate amqpTemplate(final ConnectionFactory connectionFactory) {

		final AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareQueue(chargingModuleQueue());

		final RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory);

		amqpTemplate.setMessageConverter(producerJackson2MessageConverter());
		amqpTemplate.setUserCorrelationId(true);
		amqpTemplate.setChannelTransacted(true);
		amqpTemplate.setDefaultReceiveQueue(CHARGING_MODULE_QUEUE);

		return amqpTemplate;
	}

	@Bean("spamFilterQueue")
	public Queue spamFilterQueue() {
		final boolean durable = true;
		final boolean exclusive = false;
		final boolean autoDelete = false;
		Map<String, Object> queueArgs = new HashMap<>();
		queueArgs.put("x-max-priority", 10);
		return new Queue(SPAM_FILTER_QUEUE, durable, exclusive, autoDelete, queueArgs);
	}

	@Bean("chargingModuleQueue")
	public Queue chargingModuleQueue() {
		final boolean durable = true;
		final boolean exclusive = false;
		final boolean autoDelete = false;
		Map<String, Object> queueArgs = new HashMap<>();
		queueArgs.put("x-max-priority", 10);
		return new Queue(CHARGING_MODULE_QUEUE, durable, exclusive, autoDelete, queueArgs);
	}

	@Bean("smsExchange")
	public TopicExchange smsExchange() {
		final boolean durable = true;
		final boolean autoDelete = false;
		return new TopicExchange(TOPIC_EXCHANGE_NAME, durable, autoDelete);
	}

	@Bean
	public Declarables queueBindings() {
		return new Declarables(
				BindingBuilder.bind(spamFilterQueue()).to(smsExchange())
						.with(SPAM_FILTER_BINDING_KEY),
				BindingBuilder.bind(chargingModuleQueue()).to(smsExchange())
						.with(CHARGING_MODULE_BINDING_KEY));
	}
}
