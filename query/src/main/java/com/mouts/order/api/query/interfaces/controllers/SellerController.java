package com.mouts.order.api.query.interfaces.controllers;

import com.mouts.order.api.query.application.services.OrderApplicationService;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seller")
public class SellerController implements ISellerController {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @GetMapping("/{sellerId}/orders")
    public ResponseEntity<List<OrderResponse>> getOrderBySellerId(
            @PathVariable UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var response = orderApplicationService.findBySellerId(
                sellerId,
                page,
                size);
        return response;
    }
}
