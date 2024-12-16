package com.mouts.order.api.query.interfaces.controllers;


import com.mouts.order.api.query.shared.dtos.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ICustomerController {

    @Operation(summary = "Consulta um pedido específico pelo ID do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<List<OrderResponse>> getOrderByCustomerId(
            UUID customerId,
            int page,
            int size
    );
}
