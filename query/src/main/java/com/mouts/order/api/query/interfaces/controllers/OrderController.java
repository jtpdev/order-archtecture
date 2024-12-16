package com.mouts.order.api.query.interfaces.controllers;

import com.mouts.order.api.query.application.services.OrderApplicationService;
import com.mouts.order.api.query.shared.dtos.EOrderStatus;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) EOrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = orderApplicationService.findByFilters(
                date,
                status,
                page,
                size
        );
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        var response = orderApplicationService.findById(id);
        return response;
    }
}
