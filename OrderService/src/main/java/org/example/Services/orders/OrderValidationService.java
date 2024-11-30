package org.example.Services.orders;

import lombok.RequiredArgsConstructor;

import org.example.Dtos.OrderRequestDto;
import org.example.Dtos.ProductStockRequestDto;
import org.example.Dtos.QuantityRequest;
import org.example.Models.OrderItem;
import org.example.Services.product.ProductAvailabilityService;
import org.example.Services.product.ProductInventoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderValidationService implements OrderValidator{
    private final ProductAvailabilityService productAvailabilityService;
    private final ProductInventoryService productInventoryService;

    @Override
    public void validateOrderAvailability(OrderRequestDto orderRequest) {
        for (OrderRequestDto.Item item : orderRequest.getItems()) {
            ProductStockRequestDto stockRequest = new ProductStockRequestDto();
            stockRequest.setId(item.getProductID());
            stockRequest.setQuantity(item.getQuantity());

            if (!productAvailabilityService.isProductAvailable(stockRequest)) {
                throw new RuntimeException("Product is out of stock: " + item.getProductID());
            }
        }
    }

    @Override
    public void updateInventory(OrderRequestDto orderRequest){
        for (OrderRequestDto.Item item : orderRequest.getItems()) {
            QuantityRequest quantityRequest = new QuantityRequest();
            quantityRequest.setId(item.getProductID());
            quantityRequest.setQuantity(item.getQuantity());
            productInventoryService.updateProductQuantity(quantityRequest);
        }
    }
}