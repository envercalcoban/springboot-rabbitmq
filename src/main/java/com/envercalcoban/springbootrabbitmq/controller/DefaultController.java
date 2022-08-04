package com.envercalcoban.springbootrabbitmq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envercalcoban.springbootrabbitmq.model.QueueObject;

import java.time.LocalDateTime;


@RestController
public class DefaultController
{
    @Autowired
    private AmqpTemplate defaultExchange;

    @PostMapping("default")
    public ResponseEntity<?> sendMessageWithDefaultExchange()
    {
        QueueObject object = new QueueObject("default", LocalDateTime.now());

        defaultExchange.convertAndSend(object);

        return ResponseEntity.ok(true);
    }
}
