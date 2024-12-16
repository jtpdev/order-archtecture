package com.mouts.order.api.query.interfaces.controllers;

import com.mouts.order.api.query.application.services.OrderApplicationService;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController implements ICustomerController {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<OrderResponse>> getOrderByCustomerId(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var response = orderApplicationService.findByCustomerId(
                customerId,
                page,
                size);
        return response;
    }
}
