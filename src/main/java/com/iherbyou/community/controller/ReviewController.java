package com.iherbyou.community.controller;

import com.iherbyou.community.Review;
import com.iherbyou.community.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 등록
    @PostMapping
    public Review createReview(@RequestParam Long userId, @RequestParam Long productId,
                               @RequestParam Integer rating, @RequestParam(required = false) String text) {
        return reviewService.createReview(userId, productId, rating, text);
    }

    // 상품별 리뷰 목록
    @GetMapping
    public Page<Review> listByProduct(@RequestParam Long productId, @RequestParam(required = false) Integer rating,
                                      Pageable pageable) {
        return reviewService.listByProduct(productId, rating, pageable);
    }

    // 내가 쓴 리뷰
    @GetMapping("/my")
    public Page<Review> myReviews(@RequestParam Long userId, Pageable pageable) {
        return reviewService.listMyReviews(userId, pageable);
    }

    // 리뷰 내용 수정
    @PatchMapping("/{id}")
    public void updateReviewText(@PathVariable Long id, @RequestParam Long userId,
                                 @RequestParam String newText) {
        reviewService.updateReviewText(userId, id, newText);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id, @RequestParam Long userId) {
        reviewService.softDelete(userId, id);
    }

    // 집계
    @GetMapping("/count")
    public long countByProduct(@RequestParam Long productId) {
        return reviewService.countByProduct(productId);
    }

    @GetMapping("/avg")
    public Double averageRating(@RequestParam Long productId) {
        return reviewService.averageRating(productId);
    }

    @GetMapping("/rating-count")
    public long countByRating(@RequestParam Long productId, @RequestParam int rating) {
        return reviewService.countByRating(productId, rating);
    }
}
