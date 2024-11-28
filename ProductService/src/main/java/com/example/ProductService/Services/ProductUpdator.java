package com.example.ProductService.Services;

import com.example.ProductService.Dtos.ProductRequestDto;

public interface ProductUpdator {
    void updateProduct(int productId , ProductRequestDto productRequestDto);
}
