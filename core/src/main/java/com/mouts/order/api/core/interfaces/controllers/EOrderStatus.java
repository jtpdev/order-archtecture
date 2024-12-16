package com.mouts.order.api.core.interfaces.controllers;

import com.mouts.order.api.core.domain.models.OrderStatus;

public enum EOrderStatus {
    PENDING(OrderStatus.PENDING),
    DUPLICATED(OrderStatus.DUPLICATED),
    NO_STOCK(OrderStatus.NO_STOCK),
    RECEIVED(OrderStatus.RECEIVED);

    private OrderStatus orderStatus;

    private EOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus toModel(){
        return this.orderStatus;
    }

}
