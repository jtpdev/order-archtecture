package com.mouts.order.api.core.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem{

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Id
    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    protected LocalDateTime createdAt;

}
