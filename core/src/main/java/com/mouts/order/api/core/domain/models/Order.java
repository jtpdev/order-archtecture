package com.mouts.order.api.core.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public final class Order extends Model<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "seller_id")
    private UUID sellerId;

    @Column(name = "partial")
    boolean acceptPartial;

    @Column(name = "total", precision = 18, scale = 2)
    BigDecimal total;

    public static Order create() {
        var order = new Order();
        order.id = UUID.randomUUID();
        return order;
    }
}
