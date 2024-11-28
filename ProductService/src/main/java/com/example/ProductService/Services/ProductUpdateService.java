package com.example.ProductService.Services;

import com.example.ProductService.Dtos.ProductRequestDto;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductUpdateService implements ProductUpdator {
    private final ProductRepository productRepository;

    @Override
    public void updateProduct(int productId , ProductRequestDto productRequestDto) {
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
}
