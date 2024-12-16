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

@WebMvcTest(SellerController.class)
public class SellerControllerTest {

    @MockBean
    private OrderApplicationService orderApplicationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrderBySellerId_Success() throws Exception {
        UUID sellerId = UUID.randomUUID();
        OrderResponse orderResponse = new OrderResponse(
                UUID.randomUUID(),
                "PENDING",
                null,
                null,
                UUID.randomUUID(),
                sellerId,
                null
        );

        when(orderApplicationService.findBySellerId(sellerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of(orderResponse)));

        mockMvc.perform(get("/seller/{sellerId}/orders", sellerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetOrderBySellerId_EmptyList() throws Exception {
        UUID sellerId = UUID.randomUUID();

        when(orderApplicationService.findBySellerId(sellerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/seller/{sellerId}/orders", sellerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetOrderBySellerId_InvalidSellerId() throws Exception {
        UUID invalidSellerId = UUID.randomUUID();

        when(orderApplicationService.findBySellerId(invalidSellerId, 0, 10))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/seller/{sellerId}/orders", invalidSellerId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
