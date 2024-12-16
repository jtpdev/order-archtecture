package com.mouts.order.api.query.interfaces.controllers;

import com.mouts.order.api.query.application.services.OrderApplicationService;
import com.mouts.order.api.query.shared.dtos.EOrderStatus;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderApplicationService orderApplicationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrders_Success() throws Exception {
        UUID customerId = UUID.randomUUID();
        EOrderStatus status = EOrderStatus.PENDING;
        LocalDateTime date = LocalDateTime.now();

        OrderResponse orderResponse = new OrderResponse(
                UUID.randomUUID(),
                "PENDING",
                date,
                null,
                customerId,
                UUID.randomUUID(),
                null
        );

        when(orderApplicationService.findByFilters(date, status, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of(orderResponse)));

        mockMvc.perform(get("/orders")
                        .param("date", date.toString())
                        .param("status", status.name())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetOrders_EmptyList() throws Exception {
        UUID customerId = UUID.randomUUID();
        EOrderStatus status = EOrderStatus.PENDING;
        LocalDateTime date = LocalDateTime.now();

        when(orderApplicationService.findByFilters(date, status, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/orders")
                        .param("date", date.toString())
                        .param("status", status.name())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetOrderById_Success() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponse orderResponse = new OrderResponse(
                orderId,
                "PENDING",
                LocalDateTime.now(),
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                null
        );

        when(orderApplicationService.findById(orderId)).thenReturn(ResponseEntity.ok(orderResponse));

        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testGetOrderById_NotFound() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderApplicationService.findById(orderId)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
