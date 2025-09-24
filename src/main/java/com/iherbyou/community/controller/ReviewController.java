package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
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
    // Header: X-USER-ID
    // Body: ReviewCreateRequest { productId, rating(1~5), text(optional) }
    // Response: ReviewProduct 핵심 필드만 반환 -> 응답 과다 피드백
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
                saved.getUser().getName(), // 개인정보 정책에 따라 마스킹 가능
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.ok(res);
    }

    // 상품별 리뷰 목록
    // Query: productId, rating(선택: 별점 필터)
    // Response: Page<ReviewProduct> 필드 최소화 -> 응답 과다 피드백 반영
    @GetMapping
    public ResponseEntity<Page<ReviewProduct>> listByProduct(
            @RequestParam Long productId,
            @RequestParam(required = false) Integer rating,
            Pageable pageable
    ) {
        Page<Review> page = reviewService.listByProduct(productId, rating, pageable);
        Page<ReviewProduct> body = page.map(r -> new ReviewProduct(
                r.getId(),
                r.getRating(),
                r.getText(),
                r.getUser().getName(), // 필요시 마스킹
                r.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }

    // 내가 쓴 리뷰
    // Header: X-USER-ID
    // Response: Page<ReviewProduct>
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

    // 리뷰 통계 요약 (count/avg/rating-count 통합)
    // Query: productId
    // Response: ReviewSummary { totalCount, average, counts[1~5] }
    // 카운트/평균 나뉘어 있음 -> 단일 summary로 합침
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
