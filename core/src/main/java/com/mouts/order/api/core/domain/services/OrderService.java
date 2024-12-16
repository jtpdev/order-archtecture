package com.mouts.order.api.core.domain.services;

import com.mouts.order.api.core.domain.models.Order;
import com.mouts.order.api.core.domain.models.OrderStatus;
import com.mouts.order.api.core.domain.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Page<Order> getOrdersWithFilters(OrderFilters filters) {

        PageRequest pageRequest = PageRequest.of(filters.page(), filters.size());

        Example<Order> example = Example.of(filters.toModel());
        return orderRepository.findAll(example, pageRequest);
    }

    public Order saveOrder(Order order) {

        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

}

