package com.example.ProductService.Services;

import com.example.ProductService.Dtos.AvailableRequest;
import com.example.ProductService.Dtos.OrderMessage;

public interface ProductInventory {
    void updateQuantity(int productId, int quantityChange);
    Boolean isAvailable(AvailableRequest availableRequest);
    OrderMessage checkInventory(OrderMessage orderMessage);
    void updateProductInventory(OrderMessage orderMessage);
}
