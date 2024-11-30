package com.example.ProductService.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String imageUrl;
    private String category;
    private String sellerId;
    private int popularity;
    private int rating;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;
}
