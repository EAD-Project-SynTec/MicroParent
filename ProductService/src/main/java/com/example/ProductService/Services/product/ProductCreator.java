package com.example.ProductService.Services.product;

import com.example.ProductService.Dtos.ProductRequestDto;

public interface ProductCreator {
    void saveProduct(ProductRequestDto productRequestDto);
}
