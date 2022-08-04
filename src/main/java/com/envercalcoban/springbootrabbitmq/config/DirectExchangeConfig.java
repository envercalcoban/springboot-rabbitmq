package com.envercalcoban.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DirectExchangeConfig {
	@Autowired
	private AmqpAdmin amqpAdmin;

	@Value("${rabbitmq.direct.queue-1}")
	private String directQueueOne;

	@Value("${rabbitmq.direct.queue-2}")
	private String directQueueTwo;

	@Value("${rabbitmq.direct.exchange}")
	private String directExChange;

	@Value("${rabbitmq.direct.routing-key-1}")
	private String direct_routing_key_1;

	@Value("${rabbitmq.direct.routing-key-2}")
	private String direct_routing_key_2;

	@Value("${rabbitmq.direct.deead-letter-queue-1}")
	private String direct_dead_letter_queeue_1;

	Queue createDirectQueue1() {
		return QueueBuilder.durable(directQueueOne).deadLetterExchange("")// Default Exchange
				.deadLetterRoutingKey(direct_dead_letter_queeue_1).build();

	}

	Queue createDirectQueue2() {
		return new Queue(directQueueTwo, true, false, false);
	}

	Queue createDeadLetterQueue1() {
		return new Queue(direct_dead_letter_queeue_1, true, false, false);
	}

	DirectExchange createDirectExchange() {
		return new DirectExchange(directExChange, true, false);
	}

	Binding createDirectBinding1() {
		return BindingBuilder.bind(createDirectQueue1()).to(createDirectExchange()).with(direct_routing_key_1);
	}

	Binding createDirectBinding2() {
		return BindingBuilder.bind(createDirectQueue2()).to(createDirectExchange()).with(direct_routing_key_2);
	}

	@Bean
	public AmqpTemplate directExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setExchange(directExChange);

		return rabbitTemplate;
	}

	@PostConstruct
	public void init() {
		amqpAdmin.declareQueue(createDirectQueue1());
		amqpAdmin.declareQueue(createDirectQueue2());
		amqpAdmin.declareQueue(createDeadLetterQueue1());
		amqpAdmin.declareExchange(createDirectExchange());
		amqpAdmin.declareBinding(createDirectBinding1());
		amqpAdmin.declareBinding(createDirectBinding2());
	}
}
