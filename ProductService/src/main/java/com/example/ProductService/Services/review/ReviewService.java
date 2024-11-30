package com.example.ProductService.Services.review;

import com.example.ProductService.Dtos.ReviewReplyDto;
import com.example.ProductService.Dtos.ReviewRequest;
import com.example.ProductService.Dtos.ReviewUpdateDto;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Models.Review;
import com.example.ProductService.Repository.ProductRepository;
import com.example.ProductService.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public void createReview(ReviewRequest reviewRequest) {
        if(reviewRequest.getRating()<=5 && reviewRequest.getRating()>=0){
            Review review = new Review();
            review.setComment(reviewRequest.getComment());
            review.setProductId(reviewRequest.getProductId());
            review.setReviewImageUrl(reviewRequest.getReviewImageUrl());
            review.setRating(reviewRequest.getRating());
            reviewRepository.save(review);
            updateProductTotalReview(reviewRequest.getProductId());
        }
    }

    private void updateProductTotalReview(int productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (!reviews.isEmpty()) {
            int averageRating = (int) reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            product.setRating(averageRating);
            productRepository.save(product);
        }
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByProductId(int productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void addReply(ReviewReplyDto reviewReply) {
        Review review = reviewRepository.findById(reviewReply.getId())
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        review.setReply(reviewReply.getReply());
        reviewRepository.save(review);
    }

    public void updateReview(ReviewUpdateDto reviewUpdate) {
        Review review = reviewRepository.findById(reviewUpdate.getId())
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        review.setComment(reviewUpdate.getComment());
        reviewRepository.save(review);
    }

}
