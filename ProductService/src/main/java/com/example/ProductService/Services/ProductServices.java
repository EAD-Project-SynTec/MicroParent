package com.example.ProductService.Services;

import com.example.ProductService.Dtos.*;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import com.example.ProductService.messaging.OrderMessageConsumer;
import com.example.ProductService.messaging.OrderResponseProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServices {
    private final ProductRepository productRepository;
    private final OrderResponseProducer orderResponseProducer;
    public void createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setDescription(productRequestDto.getDescription());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setCategory(productRequestDto.getCategory());
        product.setSellerId(productRequestDto.getSellerId());
        product.setPopularity(productRequestDto.getPopularity());
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        if(products.isEmpty()) {
            throw new NoSuchElementException();
        }
        return products;
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }

    public void updateProductDetails(int productId ,ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setDescription(productRequestDto.getDescription());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setCategory(productRequestDto.getCategory());
        product.setSellerId(productRequestDto.getSellerId());
        productRepository.save(product);
}

    public List<Product> sortedByPopularity(List<Product> list) {
        return list.stream().sorted(Comparator.comparing(Product::getPopularity).reversed()).collect(Collectors.toList());
    }

    public List<Product> getProductsByName(String name) {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsSortedByPrice(String order) {
        List<Product> allProducts = productRepository.findAll();

        if ("asc".equalsIgnoreCase(order)) {
            return allProducts.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        } else if ("desc".equalsIgnoreCase(order)) {
            return allProducts.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid sort order. Use 'asc' or 'desc'.");
        }
    }

    // RabbitMq Listener function---------------------------------------------------------------->
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

    //check product inventory--------------------------------------------------------------------->
    public OrderMessage checkInventory(OrderMessage orderMessage) {
        //check availability
        for(OrderItem Item : orderMessage.getItems()){
            AvailableRequest availableRequest = new AvailableRequest();
            availableRequest.setId(Item.getProductID());
            availableRequest.setQuantity(Item.getQuantity());
            if(!checkAvailability(availableRequest)){
                throw new RuntimeException("Product is out of stock " + Item.getProductID());
            }
        }
        //update product quantity
        for(OrderItem Item : orderMessage.getItems()){
           updateProductQuantity(Item.getProductID(), Item.getQuantity());
        }
        return orderMessage;
    }

    // check availability of a product----------------------------------------------------------->
    public Boolean checkAvailability(AvailableRequest availableRequest) {
        Optional<Product> product = productRepository.findById(availableRequest.getId());
        return product.isPresent() && product.get().getQuantity() >= availableRequest.getQuantity();
    }

    // update the inventory---------------------------------------------------------------------->
    public void updateProductQuantity(int productId, int quantityChange) {
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
