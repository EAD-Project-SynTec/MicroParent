package com.example.ProductService.Services.product;

import com.example.ProductService.Dtos.AvailableRequest;
import com.example.ProductService.Dtos.OrderItem;
import com.example.ProductService.Dtos.OrderMessage;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import com.example.ProductService.messaging.OrderResponseProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductInventoryService implements ProductInventory {
    private final OrderResponseProducer orderResponseProducer;
    private final ProductRepository productRepository;

    // RabbitMq Listener function
    @Override
    public void updateProductInventory(OrderMessage orderMessage) {
        try {
            OrderMessage response = checkInventory(orderMessage);
            if (response != null) {
                orderResponseProducer.sendOrderResponse(orderMessage, true);
            }
        } catch (RuntimeException e) {
            orderResponseProducer.sendOrderResponse(orderMessage, false);
        }
    }
    @Override
    public OrderMessage checkInventory(OrderMessage orderMessage) {
        //check availability
        for(OrderItem Item : orderMessage.getItems()){
            AvailableRequest availableRequest = new AvailableRequest();
            availableRequest.setId(Item.getProductID());
            availableRequest.setQuantity(Item.getQuantity());
            if(!isAvailable(availableRequest)){
                throw new RuntimeException("Product is out of stock " + Item.getProductID());
            }
        }
        //update product quantity
        for(OrderItem Item : orderMessage.getItems()){
            updateQuantity(Item.getProductID(), Item.getQuantity());
        }
        return orderMessage;
    }

    @Override
    // check availability of a product
    public Boolean isAvailable(AvailableRequest availableRequest) {
        Optional<Product> product = productRepository.findById(availableRequest.getId());
        return product.isPresent() && product.get().getQuantity() >= availableRequest.getQuantity();
    }


    // update the inventory---------------------------------------------------------------------->
    @Override
    public void updateQuantity(int productId, int quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
        if(product.getQuantity() > quantityChange) {
            int updatedQuantity = product.getQuantity() - quantityChange;
            if (updatedQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
            }
            product.setQuantity(updatedQuantity);
            // increment the popularity when order a product
            product.setPopularity(product.getPopularity() + 1);
            productRepository.save(product);
        }else {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
    }

}
