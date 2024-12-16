package com.mouts.order.api.core.interfaces.controllers;

import com.mouts.order.api.core.application.services.OrderApplicationService;
import com.mouts.order.api.core.shared.dtos.OrderRequest;
import com.mouts.order.api.core.shared.dtos.OrderResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderApplicationService orderApplicationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrder_Success() throws Exception {
        Mockito.doNothing().when(orderApplicationService).saveOrder(any(OrderRequest.class));

        String validOrderJson = """
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

        mockMvc.perform(post("/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateOrder_InvalidRequest() throws Exception {
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrders_Success() throws Exception {
        var mockResponse = List.of(
                new OrderResponse(UUID.randomUUID(), "PENDING", LocalDateTime.now(), List.of(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100)),
                new OrderResponse(UUID.randomUUID(), "COMPLETED", LocalDateTime.now(), List.of(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(200))
        );
        Mockito.when(orderApplicationService.findByFilters(any(), any(), any(), any(), eq(0), eq(10)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/orders")
                        .param("date", "2024-12-16T00:00:00")
                        .param("status", "PENDING")
                        .param("customerId", UUID.randomUUID().toString())
                        .param("sellerId", UUID.randomUUID().toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetOrders_Empty() throws Exception {
        Mockito.when(orderApplicationService.findByFilters(any(), any(), any(), any(), eq(0), eq(10)))
                .thenReturn(List.of());

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetOrderById_Success() throws Exception {
        var mockResponse = new OrderResponse(
                UUID.randomUUID(),
                "PENDING",
                LocalDateTime.now(),
                List.of(new OrderResponse.OrderItem(UUID.randomUUID(), 2, BigDecimal.valueOf(50), BigDecimal.valueOf(100))),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(100)
        );
        Mockito.when(orderApplicationService.findById(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/orders/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.value").value(100));
    }

    @Test
    public void testGetOrderById_NotFound() throws Exception {
        Mockito.when(orderApplicationService.findById(any())).thenReturn(null);

        mockMvc.perform(get("/orders/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
