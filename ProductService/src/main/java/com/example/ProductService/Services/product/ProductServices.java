package com.example.ProductService.Services.product;

import com.example.ProductService.Dtos.*;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServices {
    private final ProductRepository productRepository;
    private final ProductRetriever productRetriever;
    private final ProductInventory productInventory;
    private final ProductUpdator productUpdator;
    private final ProductCreator productCreator;

    public void createProduct(ProductRequestDto productRequestDto) {
        productCreator.saveProduct(productRequestDto);
    }

    public List<Product> getAllProducts() {
        return productRetriever.getProduct();
    }

    public Product getProductById(int id) {
        return productRetriever.getProduct(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRetriever.getProduct(category);
    }

    public void deleteProductById(int id) {
            productUpdator.removeProduct(id);
    }

    public void updateProductDetails(int productId ,ProductRequestDto productRequestDto) {
        productUpdator.updateProduct(productId, productRequestDto);
    }

    public List<Product> sortedByPopularity(List<Product> list) {
        return productRetriever.getProductsByPopularity(list);
    }

    public List<Product> getProductsByName(String name) {
        return productRetriever.getProductsByName(name);
    }

    public List<Product> getProductsSortedByPrice(String order) {
       return productRetriever.getProductsByPrice(order);
    }

    // RabbitMq Listener function---------------------------------------------------------------->
    public void updateProductInventory(OrderMessage orderMessage) {
       productInventory.updateProductInventory(orderMessage);
    }

    // check availability of a product----------------------------------------------------------->
    public Boolean checkAvailability(AvailableRequest availableRequest) {
        return productInventory.isAvailable(availableRequest);
    }

    // update the inventory---------------------------------------------------------------------->
    public void updateProductQuantity(int productId, int quantityChange) {
        productInventory.updateQuantity(productId, quantityChange);
    }


}
