package com.envercalcoban.springbootrabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envercalcoban.springbootrabbitmq.consumer.ManualConsumer;

@RestController
public class ManualConsumerController {
	@Autowired
	private ManualConsumer manualConsumer;

	@PostMapping("consume/{queueName}")
	public ResponseEntity<?> consumeAllMessagesOfQueue(@PathVariable String queueName) {
		return ResponseEntity.ok(manualConsumer.receiveMessages(queueName));
	}
}
