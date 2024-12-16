package com.mouts.order.api.core.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouts.order.api.core.application.services.OrderApplicationService;
import com.mouts.order.api.core.shared.dtos.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaConsumer.class);

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-topic",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        try {
            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);

            orderApplicationService.saveOrder(orderRequest);

            logger.info("Pedido processado com sucesso: {}", orderRequest);
        } catch (Exception e) {
            logger.error("Erro ao processar a mensagem: {}", e.getMessage(), e);
        }
    }
}
