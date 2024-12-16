package com.mouts.order.api.core.domain.repositories;

import com.mouts.order.api.core.domain.models.Order;
import com.mouts.order.api.core.domain.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findById(UUID id);

    @Query(value = "SELECT * FROM orders o " +
        "WHERE (:date IS NULL OR o.date = :date) " +
        "AND (:customerId IS NULL OR o.customer_id = :customerId) " +
        "AND (:sellerId IS NULL OR o.seller_id = :sellerId) " +
        "AND (:orderStatus IS NULL OR o.status_id = :orderStatus)",
        nativeQuery = true)
    Page<Order> findByFiltersWithOr(
            LocalDateTime date,
            UUID customerId,
            UUID sellerId,
            OrderStatus orderStatus,
            Pageable pageable);

}
