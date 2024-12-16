package com.mouts.order.api.core.application.services;

import com.mouts.order.api.core.domain.models.Order;
import com.mouts.order.api.core.domain.models.OrderItem;
import com.mouts.order.api.core.domain.models.OrderStatus;
import com.mouts.order.api.core.domain.services.OrderFilters;
import com.mouts.order.api.core.domain.services.OrderService;
import com.mouts.order.api.core.infrastructure.grpc.Product;
import com.mouts.order.api.core.infrastructure.grpc.ProductServiceClient;
import com.mouts.order.api.core.shared.dtos.OrderRequest;
import com.mouts.order.api.core.shared.dtos.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(OrderApplicationService.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductServiceClient productServiceClient;

    public OrderResponse findById(UUID id){
        var order = orderService.getOrderById(id).orElseGet(null);

        if (order == null) {
            return null;
        }

        return new OrderResponse(
                order.getId(),
                order.getStatus().getTitle(),
                order.getDate(),
                order.getItems().stream().map(i -> new OrderResponse.OrderItem(i.getItemId(), i.getAmount(), i.getValue(), i.getTotal())).toList(),
                order.getCustomerId(),
                order.getSellerId(),
                order.getTotal()
        );
    }

    public List<OrderResponse> findByFilters(
            LocalDateTime date,
            UUID customerId,
            UUID sellerId,
            OrderStatus orderStatus,
            int page,
            int size) {

        var filters = new OrderFilters(
                date,
                customerId,
                sellerId,
                orderStatus,
                page,
                size
        );

        var orders = orderService.getOrdersWithFilters(filters);
        return orders.map(o -> new OrderResponse(
                o.getId(),
                o.getStatus().getTitle(),
                o.getDate(),
                o.getItems().stream().map(i -> new OrderResponse.OrderItem(i.getItemId(), i.getAmount(), i.getValue(), i.getTotal())).toList(),
                o.getCustomerId(),
                o.getSellerId(),
                o.getTotal()
        )).toList();
    }

    public void saveOrder(OrderRequest orderRequest) {
        var filters = new OrderFilters(
                orderRequest.date(),
                orderRequest.customerId(),
                orderRequest.sellerId(),
                null,
                0,
                1
        );
        var orders = orderService.getOrdersWithFilters(filters);
        if (orders.getTotalElements() > 0) {
            logger.info("Pedido duplicado: {}", orderRequest);
            return;
        }
        var items = orderRequest.items().stream().filter(i -> i.quantity() > 0).toList();
        var productIds = items.stream().map(i -> i.id().toString()).toList();
        var productExistences = productServiceClient.checkProductsExistence(productIds);
        if (!hasSufficientStock(productExistences, items) && !orderRequest.acceptPartial()) {
            logger.info("Pedido sem estoque: {}", orderRequest);
            return;
        }

        var orderValue = calculateTotalOrderValue(productExistences, items);

        var order = Order.create();
        order.setStatus(OrderStatus.RECEIVED);
        order.setDate(orderRequest.date());
        order.setItems(mapToOrderItems(productExistences, items, order.getId()));
        order.setCustomerId(orderRequest.customerId());
        order.setSellerId(orderRequest.sellerId());
        order.setAcceptPartial(orderRequest.acceptPartial());
        order.setTotal(BigDecimal.valueOf(orderValue));

        orderService.saveOrder(order);
    }

    private boolean hasSufficientStock(List<Product.ProductExistence> productExistences, List<OrderRequest.OrderItem> orderItems) {
        var productMap = productExistences.stream()
                .collect(Collectors.toMap(Product.ProductExistence::getProductId, product -> product));

        return orderItems.stream()
                .allMatch(orderItem -> {
                    var product = productMap.get(orderItem.id().toString());
                    return product != null && product.getStockAmount() >= orderItem.quantity();
                });
    }

    private List<OrderItem> mapToOrderItems(List<Product.ProductExistence> productExistences, List<OrderRequest.OrderItem> orderItems, UUID orderId) {
        return orderItems.stream()
                .map(orderItem -> {
                    var productExistence = productExistences.stream()
                            .filter(product -> product.getProductId().equals(orderItem.id().toString()))
                            .findFirst()
                            .orElse(null);

                    if (productExistence == null) {
                        return null;
                    }


                    UUID itemId = orderItem.id();
                    Integer amount = orderItem.quantity();
                    BigDecimal value = BigDecimal.valueOf(productExistence.getValue());
                    BigDecimal total = value.multiply(BigDecimal.valueOf(amount));

                    return new OrderItem(
                            orderId,
                            itemId,
                            amount,
                            value,
                            total,
                            LocalDateTime.now());
                })
                .filter(orderItem -> orderItem != null)
                .collect(Collectors.toList());
    }

    private double calculateTotalOrderValue(List<Product.ProductExistence> productExistences, List<OrderRequest.OrderItem> orderItems) {
        var productMap = productExistences.stream()
                .collect(Collectors.toMap(Product.ProductExistence::getProductId, product -> product));

        return orderItems.stream()
                .mapToDouble(orderItem -> {
                    var product = productMap.get(orderItem.id().toString());
                    if (product == null) {
                        return 0d;
                    }
                    var multiplier = orderItem.quantity() > product.getStockAmount() ? product.getStockAmount() : orderItem.quantity();
                    return product.getValue() * multiplier;
                })
                .sum();
    }
}
