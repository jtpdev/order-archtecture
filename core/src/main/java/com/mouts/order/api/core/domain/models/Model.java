package com.mouts.order.api.core.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract sealed class Model<T> permits Order, OrderStatus {

    @Id
    protected T id;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    protected LocalDateTime createdAt;
}
