package com.example.ProductService.Services.product;

import com.example.ProductService.Models.Product;

import java.util.List;

public interface ProductRetriever {
    List<Product> getProduct();
    List<Product> getProduct(String category);
    Product getProduct(int id);
    List<Product> getProductsByPrice(String order);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByPopularity(List<Product> list);
    }
