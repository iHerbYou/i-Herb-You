package com.iherbyou.community.controller;

import com.iherbyou.community.dto.ReviewCreateRequest;
import com.iherbyou.community.dto.ReviewProduct;
import com.iherbyou.community.dto.ReviewSummary;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewProduct> createReview(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody ReviewCreateRequest req
    ) {
        Review saved = reviewService.createReview(userId, req.productId(), req.rating(), req.text());
        ReviewProduct res = new ReviewProduct(
                saved.getId(),
                saved.getRating(),
                saved.getText(),
                saved.getUser().getName(),
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.ok(res);
    }

    // 상품별 리뷰 목록 (rating 파라미터 제거)
    @GetMapping
    public ResponseEntity<Page<ReviewProduct>> listByProduct(
            @RequestParam Long productId,
            Pageable pageable
    ) {
        Page<Review> page = reviewService.listByProduct(productId, pageable);
        Page<ReviewProduct> body = page.map(r -> new ReviewProduct(
                r.getId(),
                r.getRating(),
                r.getText(),
                r.getUser().getName(),
                r.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }

    // 내가 쓴 리뷰
    @GetMapping("/my")
    public ResponseEntity<Page<ReviewProduct>> myReviews(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        Page<Review> page = reviewService.listMyReviews(userId, pageable);
        Page<ReviewProduct> body = page.map(r -> new ReviewProduct(
                r.getId(),
                r.getRating(),
                r.getText(),
                r.getUser().getName(),
                r.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }

    // 리뷰 통계 요약 (총개수/평균/별점분포)
    @GetMapping("/summary")
    public ResponseEntity<ReviewSummary> summary(@RequestParam Long productId) {
        long total = reviewService.countByProduct(productId);
        double avg = reviewService.averageRating(productId);
        long[] counts = new long[5];
        for (int i = 1; i <= 5; i++) {
            counts[i - 1] = reviewService.countByRating(productId, i);
        }
        return ResponseEntity.ok(new ReviewSummary(total, avg, counts));
    }
}
