package com.mouts.order.api.query.application.services;

import com.mouts.order.api.query.infrastructure.clients.OrderClient;
import com.mouts.order.api.query.shared.dtos.EOrderStatus;
import com.mouts.order.api.query.shared.dtos.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(OrderApplicationService.class);

    @Autowired
    private OrderClient orderClient;

    public ResponseEntity<OrderResponse> findById(UUID id){
        return orderClient.getOrderById(id);
    }

    public ResponseEntity<List<OrderResponse>> findByFilters(
            LocalDateTime date,
            EOrderStatus orderStatus,
            int page,
            int size) {

        return orderClient.getOrders(date,
                orderStatus,
                null,
                null,
                page,
                size);
    }

    public ResponseEntity<List<OrderResponse>> findByCustomerId(
            UUID customerID,
            int page,
            int size) {

        return orderClient.getOrders(null,
                null,
                customerID,
                null,
                page,
                size);
    }

    public ResponseEntity<List<OrderResponse>> findBySellerId(
            UUID sellerID,
            int page,
            int size) {

        return orderClient.getOrders(null,
                null,
                null,
                sellerID,
                page,
                size);
    }

}
