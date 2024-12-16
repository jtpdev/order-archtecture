package com.mouts.order.api.core.interfaces.controllers;


import com.mouts.order.api.core.shared.dtos.OrderRequest;
import com.mouts.order.api.core.shared.dtos.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IOrderController {

    @Operation(summary = "Cria um novo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequestDTO);

    @Operation(summary = "Consulta pedidos com filtros e paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de consulta inválidos", content = @Content)
    })
    ResponseEntity<List<OrderResponse>> getOrders(
            LocalDateTime date,
            EOrderStatus status,
            UUID customerId,
            UUID sellerId,
            int page,
            int size
    );

    @Operation(summary = "Consulta um pedido específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<OrderResponse> getOrderById(UUID id);
}
