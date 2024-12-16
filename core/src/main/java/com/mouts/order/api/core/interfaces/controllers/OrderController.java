package com.mouts.order.api.core.interfaces.controllers;

import com.mouts.order.api.core.application.services.OrderApplicationService;
import com.mouts.order.api.core.shared.dtos.OrderRequest;
import com.mouts.order.api.core.shared.dtos.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController implements IOrderController{

    @Autowired
    private OrderApplicationService orderApplicationService;

    @PostMapping("/")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequestDTO) {
        orderApplicationService.saveOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) EOrderStatus status,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = orderApplicationService.findByFilters(
                date,
                customerId,
                sellerId,
                status == null ? null : status.toModel(),
                page,
                size
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        var response = orderApplicationService.findById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
