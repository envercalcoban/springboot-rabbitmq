package com.envercalcoban.springbootrabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootRabbitmqApplication {



	@Value("${spring.rabbitmq.username}")
	private String userName;

	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private int port;

	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setUsername(userName);
		cachingConnectionFactory.setPassword(password);
		cachingConnectionFactory.setPort(port);
		cachingConnectionFactory.setHost(host);
		cachingConnectionFactory.setVirtualHost(virtualHost);
		return cachingConnectionFactory;
	}

	@Bean
	public MessageConverter messageConverter() {
		ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
		SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory());
		simpleRabbitListenerContainerFactory.setMessageConverter(messageConverter());
		simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(10);
		simpleRabbitListenerContainerFactory.setConcurrentConsumers(5);
		simpleRabbitListenerContainerFactory.setAutoStartup(true);
		simpleRabbitListenerContainerFactory.setPrefetchCount(10);
		return simpleRabbitListenerContainerFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRabbitmqApplication.class, args);
	}

}
