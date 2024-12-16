package com.mouts.order.api.command.interfaces.controllers;

import com.mouts.order.api.command.infrastructure.messaging.OrderKafkaProducer;
import com.mouts.order.api.command.shared.dtos.OrderRequest;
import com.mouts.order.api.command.shared.dtos.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController implements IOrderController{

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    @PostMapping("/")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequestDTO) {
        orderKafkaProducer.sendMessage(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
