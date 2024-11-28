package org.example.Services.product;

import lombok.RequiredArgsConstructor;
import org.example.client.ProductClient;
import org.example.client.Product;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailsService {
    private final ProductClient productClient;

    public Product getProductDetails(int productId) {
        return productClient.getProductById(productId);
    }
}
