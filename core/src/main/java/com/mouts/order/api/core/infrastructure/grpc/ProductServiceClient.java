package com.mouts.order.api.core.infrastructure.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceClient {

    private final ManagedChannel channel;
    private final ProductServiceGrpc.ProductServiceBlockingStub blockingStub;

    public ProductServiceClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.blockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public List<Product.ProductExistence> checkProductsExistence(List<String> productIds) {
        Product.ProductRequest request = Product.ProductRequest.newBuilder()
                .addAllProductIds(productIds)
                .build();

        Product.ProductResponse response = blockingStub.checkProductsExistence(request);

        return response.getProductExistenceList();
    }

    public void close() {
        channel.shutdown();
    }
}
