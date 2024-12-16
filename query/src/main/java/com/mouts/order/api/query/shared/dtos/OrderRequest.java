package com.mouts.order.api.query.shared.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
        UUID id,
        LocalDateTime date,
        List<OrderItem> items,
        UUID customerId,
        UUID sellerId,
        boolean acceptPartial
) {

    public record OrderItem(UUID id, Integer quantity) {
    }

}