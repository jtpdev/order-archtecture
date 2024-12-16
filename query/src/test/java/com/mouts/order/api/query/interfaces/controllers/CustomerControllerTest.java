package com.mouts.order.api.query.interfaces.controllers;

import com.mouts.order.api.query.application.services.OrderApplicationService;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private OrderApplicationService orderApplicationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrderByCustomerId_Success() throws Exception {
        UUID customerId = UUID.randomUUID();
        OrderResponse orderResponse = new OrderResponse(
                UUID.randomUUID(),
                "PENDING",
                null,
                null,
                customerId,
                UUID.randomUUID(),
                null
        );

        when(orderApplicationService.findByCustomerId(customerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of(orderResponse)));

        mockMvc.perform(get("/customers/{customerId}/orders", customerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetOrderByCustomerId_EmptyList() throws Exception {
        UUID customerId = UUID.randomUUID();

        when(orderApplicationService.findByCustomerId(customerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/customers/{customerId}/orders", customerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetOrderByCustomerId_InvalidCustomerId() throws Exception {
        UUID invalidCustomerId = UUID.randomUUID();

        when(orderApplicationService.findByCustomerId(invalidCustomerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/customers/{customerId}/orders", invalidCustomerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
