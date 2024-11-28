package org.example.Services.product;
import lombok.RequiredArgsConstructor;
import org.example.Dtos.ProductStockRequestDto;
import org.example.client.ProductClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAvailabilityService {
    private final ProductClient productClient;

    public boolean isProductAvailable(ProductStockRequestDto stockRequest) {
        return productClient.isInStock(stockRequest);
    }
}
