package com.example.ProductService.Services.product;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRetrieveService implements ProductRetriever {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getProduct() {
        return productRepository.findByIsDeletedFalse();
    }

    @Override
    public Product getProduct(int id) {
        // Ensure the product is not deleted
        return productRepository.findById(id)
                .filter(product -> !product.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Product not found or is deleted."));
    }

    @Override
    public List<Product> getProduct(String category) {
        List<Product> products = productRepository.findByCategory(category)
                .stream()
                .filter(product -> !product.isDeleted())
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found in this category or they are deleted.");
        }
        return products;
    }

    @Override
    public List<Product> getProductsByPopularity(List<Product> list) {
        return list.stream()
                .filter(product -> !product.isDeleted())
                .sorted(Comparator.comparing(Product::getPopularity).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByName(String name) {
        List<Product> allProducts = getProduct();
        return allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPrice(String order) {
        List<Product> allProducts = getProduct();
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

}
