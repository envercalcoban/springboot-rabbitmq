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
public class TopicExchangeConfig
{
    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.topic.queue-1}")
    private String queueName1;

    @Value("${rabbitmq.topic.queue-2}")
    private String queueName2;

    @Value("${rabbitmq.topic.queue-3}")
    private String queueName3;

    @Value("${rabbitmq.topic.exchange}")
    private String topicExchange;

    @Value("${rabbitmq.topic.pattern-1}")
    private String topicPattern1;

    @Value("${rabbitmq.topic.pattern-2}")
    private String topicPattern2;

    @Value("${rabbitmq.topic.pattern-3}")
    private String topicPattern3;

    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createTopicQueue1());
        amqpAdmin.declareQueue(createTopicQueue2());
        amqpAdmin.declareQueue(createTopicQueue3());
        amqpAdmin.declareExchange(createTopicExchange());
        amqpAdmin.declareBinding(createTopicBinding1());
        amqpAdmin.declareBinding(createTopicBinding2());
        amqpAdmin.declareBinding(createTopicBinding3());
    }
    
   
    @Bean
    public AmqpTemplate topicExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(topicExchange);

        return rabbitTemplate;
    }

 
    
    
   Queue createTopicQueue1(){
    	
        return new Queue(queueName1, true, false, false);
    }

    Queue createTopicQueue2() {
    	
        return new Queue(queueName2, true, false, false);
    }

    Queue createTopicQueue3(){
    	
        return new Queue(queueName3, true, false, false);
    }

    TopicExchange createTopicExchange(){
    	
        return new TopicExchange(topicExchange, true, false);
    }

    Binding createTopicBinding1(){
    	
        return BindingBuilder.bind(createTopicQueue1()).to(createTopicExchange()).with(topicPattern1);
    }

    Binding createTopicBinding2(){
    	
        return BindingBuilder.bind(createTopicQueue2()).to(createTopicExchange()).with(topicPattern2);
    }

    Binding createTopicBinding3(){
    	
        return BindingBuilder.bind(createTopicQueue3()).to(createTopicExchange()).with(topicPattern3);
    }
    
}
