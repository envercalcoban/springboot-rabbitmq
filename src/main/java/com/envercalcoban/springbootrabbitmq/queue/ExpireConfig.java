package com.envercalcoban.springbootrabbitmq.queue;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ExpireConfig {
	@Autowired
	private AmqpAdmin amqpAdmin;

	@Value("${rabbitmq.expire.queue}")
	private String queueName;

	Queue createExpireQueue() {
		return QueueBuilder.durable(queueName).expires(10000).build();
	}

	@Bean
	public AmqpTemplate defaultExpireExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setRoutingKey(queueName);

		return rabbitTemplate;
	}

	@PostConstruct
	public void init() {
		amqpAdmin.declareQueue(createExpireQueue());
	}
}
