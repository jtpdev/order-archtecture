package com.mouts.order.api.core.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class OrderStatus extends Model<Integer> {

    @Column(name = "title")
    private String title;

    public OrderStatus(Integer id, String title) {
        super.id = id;
        this.title = title;
    }

    public static OrderStatus PENDING = new OrderStatus(1, "PENDING");
    public static OrderStatus DUPLICATED = new OrderStatus(2, "DUPLICATED");
    public static OrderStatus NO_STOCK = new OrderStatus(3, "NO_STOCK");
    public static OrderStatus RECEIVED = new OrderStatus(4, "RECEIVED");
}
