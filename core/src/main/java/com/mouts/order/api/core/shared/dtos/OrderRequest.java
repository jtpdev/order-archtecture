package com.mouts.order.api.core.shared.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
        @NotNull LocalDateTime date,
        @NotNull List<OrderItem> items,
        @NotNull UUID customerId,
        @NotNull UUID sellerId,
        boolean acceptPartial
) {

    public record OrderItem(
            @NotNull UUID id,
            @NotNull Integer quantity) {
    }

}