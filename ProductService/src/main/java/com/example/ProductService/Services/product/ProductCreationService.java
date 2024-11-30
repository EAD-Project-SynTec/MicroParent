package com.example.ProductService.Services.product;

import com.example.ProductService.Dtos.ProductRequestDto;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCreationService implements ProductCreator {
    private final ProductRepository productRepository;

    @Override
    public void saveProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setDescription(productRequestDto.getDescription());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setCategory(productRequestDto.getCategory());
        product.setSellerId(productRequestDto.getSellerId());
        product.setPopularity(productRequestDto.getPopularity());
        product.setRating(0);
        productRepository.save(product);
    }
}
