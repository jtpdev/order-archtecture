package com.mouts.order.api.core.domain.services;

import com.mouts.order.api.core.domain.models.Order;
import com.mouts.order.api.core.domain.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderFilters (
        LocalDateTime date,
        UUID customerId,
        UUID sellerId,
        OrderStatus orderStatus,
        int page,
        int size) {

    public Order toModel() {
        return new Order(
                orderStatus(),
                date(),
                null,
                customerId(),
                sellerId(),
                true,
                null
        );
    }
}
