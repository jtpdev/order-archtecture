package com.mouts.order.api.query.infrastructure.clients;

import com.mouts.order.api.query.shared.dtos.EOrderStatus;
import com.mouts.order.api.query.shared.dtos.OrderRequest;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8091")  // URL para o serviço do OrderController
public interface OrderClient {

    @PostMapping("/orders/")
    ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest);

    @GetMapping("/orders")
    ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) EOrderStatus status,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @GetMapping("/orders/{id}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id);
}
