package com.mouts.order.api.command.infrastructure.messaging;

import com.mouts.order.api.command.shared.dtos.OrderRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@EnableKafka
public class OrderKafkaProducer {

    private static final String TOPIC = "order-topic";

    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderRequest> kafkaTemplate;

    public void sendMessage(OrderRequest orderRequest) {
        kafkaTemplate.send(TOPIC, orderRequest);
        logger.info("Mensagem enviada para o tópico {}: {}", TOPIC, orderRequest);
    }
}
