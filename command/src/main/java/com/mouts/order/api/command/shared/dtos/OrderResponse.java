package com.mouts.order.api.command.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String status,
        LocalDateTime date,
        List<OrderItem> items,
        UUID customerId,
        UUID sellerId,
        BigDecimal value
) {
    public record OrderItem(UUID id, Integer quantity, BigDecimal value, BigDecimal total) {
    }
}
