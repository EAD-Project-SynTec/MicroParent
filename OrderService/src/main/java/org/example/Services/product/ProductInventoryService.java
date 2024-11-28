package org.example.Services.product;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.QuantityRequest;
import org.example.client.ProductClient;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInventoryService {
    private final ProductClient productClient;

    public void updateProductQuantity(QuantityRequest quantityRequest) {
        productClient.updateQuantity(quantityRequest);
    }
}
