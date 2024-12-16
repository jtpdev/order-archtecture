package com.mouts.order.api.product.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    @Override
    public void checkProductsExistence(Product.ProductRequest request, StreamObserver<Product.ProductResponse> responseObserver) {
        var responseBuilder = Product.ProductResponse.newBuilder();

        for (String productId : request.getProductIdsList()) {
            var productExistence = Product.ProductExistence.newBuilder()
                    .setProductId(productId)
                    .setStockAmount(50)  // Quantidade de estoque mockada
                    .setValue(19.99)     // Valor do produto mockado
                    .build();
            responseBuilder.addProductExistence(productExistence);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
