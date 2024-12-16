package com.mouts.order.api.command.interfaces.controllers;

import com.mouts.order.api.command.infrastructure.messaging.OrderKafkaProducer;
import com.mouts.order.api.command.shared.dtos.OrderRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderKafkaProducer orderKafkaProducer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrder() throws Exception {
        String orderRequestJson = """
            {
                "id": "%s",
                "date": "%s",
                "items": [{"id": "%s", "quantity": 2}],
                "customerId": "%s",
                "sellerId": "%s",
                "acceptPartial": true
            }
        """.formatted(
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        Mockito.doNothing().when(orderKafkaProducer).sendMessage(any(OrderRequest.class));

        mockMvc.perform(post("/orders/")
                        .contentType("application/json")
                        .content(orderRequestJson))
                .andExpect(status().isCreated());

        Mockito.verify(orderKafkaProducer, Mockito.times(1)).sendMessage(any(OrderRequest.class));
    }

    @Test
    public void testCreateOrder_WithInvalidData() throws Exception {
        String invalidOrderJson = """
            {
                "id": "",
                "date": "",
                "items": [],
                "customerId": "",
                "sellerId": "",
                "acceptPartial": false
            }
        """;

        mockMvc.perform(post("/orders/")
                        .contentType("application/json")
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest());
    }
}
