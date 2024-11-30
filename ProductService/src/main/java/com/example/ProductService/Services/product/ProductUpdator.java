package com.example.ProductService.Services.product;

import com.example.ProductService.Dtos.ProductRequestDto;

public interface ProductUpdator {
    void updateProduct(int productId , ProductRequestDto productRequestDto);
    void removeProduct(int productId);
}
